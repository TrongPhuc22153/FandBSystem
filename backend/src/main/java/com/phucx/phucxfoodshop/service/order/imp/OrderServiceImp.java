package com.phucx.phucxfoodshop.service.order.imp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.compositeKey.OrderDetailKey;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.Invoice;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.OrderItem;
import com.phucx.phucxfoodshop.model.OrderItemDiscount;
import com.phucx.phucxfoodshop.model.OrderSummary;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.Order;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderDetailExtended;
import com.phucx.phucxfoodshop.model.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.repository.OrderDetailDiscountRepository;
import com.phucx.phucxfoodshop.repository.OrderDetailExtendedRepository;
import com.phucx.phucxfoodshop.repository.OrderDetailRepository;
import com.phucx.phucxfoodshop.repository.OrderRepository;
import com.phucx.phucxfoodshop.service.order.ConvertOrderService;
import com.phucx.phucxfoodshop.service.order.OrderService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.utils.BigDecimalUtils;
import com.phucx.phucxfoodshop.utils.LocalDateTimeConverterUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailDiscountRepository orderDetailDiscountRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailExtendedRepository orderDetailExtendedRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ConvertOrderService convertOrderService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean updateOrderStatus(String orderID, OrderStatus status) throws NotFoundException {
        log.info("updateOrderStatus(orderID={}, status={})", orderID, status);
        Order order = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " does not found"));
        Boolean check = orderRepository.updateOrderStatus(order.getOrderID(), status.name()); 
        return check;
    }

    @Override
    public String saveFullOrder(OrderWithProducts order) throws NotFoundException, CustomerNotFoundException {
        log.info("saveFullOrder({})", order);
        String orderID = this.saveOrder(order);
        // save OrderDetail
        List<OrderItem> orderItems = order.getProducts();
        for(OrderItem orderItem : orderItems) {
            OrderDetailKey orderDetailID = this.saveOrderDetail(orderID, orderItem);
            for(OrderItemDiscount discount: orderItem.getDiscounts()){
                if(discount.getDiscountID()!=null){
                    this.saveOrderDetailDiscounts(orderDetailID, discount);
                }
            }
        }
        return orderID;
    }
    // save order
    private String saveOrder(OrderWithProducts order) throws NotFoundException{
        log.info("saveOrder({})", order);
        String orderID = UUID.randomUUID().toString();
        // get order status
        String orderStatus = getStatus(order.getMethod());
        // save order 
        Boolean status = orderRepository.createOrder(
            orderID, order.getOrderDate(), order.getRequiredDate(), 
            order.getShippedDate(), order.getFreight(), order.getShipName(), 
            order.getShipAddress(), order.getShipCity(), order.getShipDistrict(),
            order.getShipWard(), order.getPhone(), orderStatus, order.getCustomerID(),
            order.getEmployeeID(), order.getShipVia());
        if(!status) throw new RuntimeException("Error while saving order for customer " + order.getCustomerID());
        
        log.info("orderID: {}", orderID);
        return orderID;
    }

    private String getStatus(String method){
        return OrderStatus.Pending.name();
    }

    // save OrderDetail
    private OrderDetailKey saveOrderDetail(String orderID, OrderItem orderItem){
        log.info("saveOrderDetail(orderID={}, orderItem={})", orderID, orderItem.toString());
        Boolean status = orderDetailRepository.createOrderDetail(
            orderItem.getProductID(), orderID, 
            BigDecimalUtils.formatter(orderItem.getUnitPrice()), 
            orderItem.getQuantity());
        if(!status) throw new RuntimeException("Error while saving orderDetail for order " + orderID + " and product " + orderItem.getProductID());
        return new OrderDetailKey(orderItem.getProductID(), orderID);
    }
    // save orderDiscount
    private void saveOrderDetailDiscounts(OrderDetailKey orderDetailID, OrderItemDiscount orderItemDiscount){
        log.info("saveOrderDetailDiscounts(orderDetailID={}, orderItemDiscount={})", orderDetailID, orderItemDiscount);
        LocalDateTime currentTime = LocalDateTime.now();
        Boolean status = orderDetailDiscountRepository.createOrderDetailDiscount(
            orderDetailID.getOrderID(), orderDetailID.getProductID(), 
            orderItemDiscount.getDiscountID(), orderItemDiscount.getDiscountPercent(), 
            currentTime);

        if(!status) throw new RuntimeException("Error while saving orderDetailDiscount for orderDetail " + orderDetailID.toString() + " and discount " + orderItemDiscount.getDiscountID());
    }

    @Override
    public ResponseFormat validateAndProcessOrder(OrderWithProducts order) throws JsonProcessingException{
        log.info("validateAndProcessOrder({})", order.toString());
        if(order==null || order.getProducts().size()==0) {
            ResponseFormat responseFormat = new ResponseFormat();
            responseFormat.setStatus(false);
            responseFormat.setError("Your order does not have any products");
            return responseFormat;
        }

        List<ProductDiscountsDTO> productDiscountsDTOs = order.getProducts().stream()
            .map((product) -> {
                // get discountIDs
                List<String> discountIDs = product.getDiscounts().stream()
                    .map(OrderItemDiscount::getDiscountID)
                    .collect(Collectors.toList());
                // get applieddate
                LocalDateTime currenTime = null;
                if(!discountIDs.isEmpty()){
                    currenTime = product.getDiscounts().get(0).getAppliedDate();
                }
                return new ProductDiscountsDTO(product.getProductID(), 
                    product.getQuantity(), discountIDs, currenTime);
            }).collect(Collectors.toList());

        // validate products
        ResponseFormat responseFormat = productService
            .validateAndProcessProducts(productDiscountsDTOs);
        return responseFormat;
    }
    
    
    @Override
    public Boolean updateOrderEmployee(String orderID, String employeeID) {
        log.info("updateOrderEmployee(orderID={}, employeeID={})", orderID, employeeID);
        Boolean check = orderRepository.updateOrderEmployeeID(orderID, employeeID);
        return check;
    }

    @Override
    public Boolean validateOrder(OrderWithProducts order) throws JsonProcessingException, InvalidDiscountException {
        log.info("validateOrder({})", order);
        // product discount DTOs
        List<ProductDiscountsDTO> productDiscountsDTOs = new ArrayList<>();
        // extract products and discounts from order
        for (OrderItem item : order.getProducts()) {
            List<String> discountIds = item.getDiscounts().stream()
                .filter(discount -> discount.getDiscountID()!=null)
                .map(OrderItemDiscount::getDiscountID)
                .collect(Collectors.toList());

            LocalDateTime appliedDate = item.getDiscounts().get(0).getAppliedDate();
            ProductDiscountsDTO productDiscountsDTO = new ProductDiscountsDTO(
                item.getProductID(), item.getQuantity(), discountIds, appliedDate);
            productDiscountsDTOs.add(productDiscountsDTO);
        }
        // validate products and discounts
        ResponseFormat isValidDiscount = productService.validateProducts(productDiscountsDTOs);       
        if(!isValidDiscount.getStatus()) throw new InvalidDiscountException(isValidDiscount.getError());
        return isValidDiscount.getStatus();
    }

    @Override
    public OrderWithProducts getPendingOrderDetail(String orderID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, ShipperNotFoundException{
        log.info("getPendingOrderDetail(orderID={})", orderID);
        Order order = orderRepository.findByOrderIDAndStatus( orderID, OrderStatus.Pending)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " with pending status does not found"));
        OrderWithProducts convertedOrder = convertOrderService.convertOrderWithProducts(order);
        return convertedOrder;
    }

    @Override
    public OrderDetails getOrder(String orderID, OrderStatus status) throws JsonProcessingException, NotFoundException, CustomerNotFoundException {
        log.info("getOrder(orderID={}, status={})", orderID, status.name());
        List<OrderDetailExtended> orderDetailExtendeds = orderDetailExtendedRepository
            .findByOrderIDAndStatus(orderID, status);
        if(orderDetailExtendeds==null || orderDetailExtendeds.size()==0) 
            throw new NotFoundException("Order "+ orderID +" with status "+ status +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtendeds);
    }

    @Override
    public OrderDetails getOrder(String orderID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException {
        log.info("getOrder(orderID={})", orderID);
        List<OrderDetailExtended> orderDetailExtended = orderDetailExtendedRepository
            .findByOrderID(orderID);
        if(orderDetailExtended==null || orderDetailExtended.size()==0) 
            throw new NotFoundException("Order "+ orderID +" does not found");
        return convertOrderService.convertOrderDetail(orderDetailExtended);
    }

    @Override
    public Boolean isPendingOrder(String orderID) throws NotFoundException {
        log.info("isPendingOrder(OrderID={})", orderID);
        Order fetchedOrder = orderRepository.findById(orderID)
            .orElseThrow(()-> new NotFoundException("Order ID " + orderID + " does not found"));
        if(fetchedOrder.getStatus().equals(OrderStatus.Pending)) return true;
        return false;
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByCustomerID(customerID={}, orderStatus={}, pageNumber={}, pageSize={})", 
            customerID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDAndStatusOrderByOrderDateDesc(customerID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public InvoiceDetails getInvoiceByCustomerID(String customerID, String orderID) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, EmployeeNotFoundException {
        log.info("getInvoiceByCustomerID(customerID={}, orderID={})", customerID, orderID);
        List<Invoice> invoices = this.getCustomerInvoices(orderID, customerID);
        log.info("Invoices: {}", invoices);
        if(invoices==null || invoices.isEmpty()) 
            throw new NotFoundException("Invoice " + orderID + " of customer "+ customerID + " does not found");
        InvoiceDetails invoice = convertOrderService.convertInvoiceDetails(invoices);
        return invoice;
    }

    // get customer invoices
    private List<Invoice> getCustomerInvoices(String orderID, String customerID){
        StoredProcedureQuery procedure = entityManager.createStoredProcedureQuery("GetCustomerInvoice")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .setParameter(1, orderID)
            .setParameter(2, customerID);
        procedure.execute();
        List<Object[]> results = procedure.getResultList();
        return results.stream().map(result ->{

            LocalDateTime orderDate = result[9]!=null?
                LocalDateTimeConverterUtils.convertToLocalDateTime(result[9]):null;
            LocalDateTime requiredDate = result[10]!=null?
                LocalDateTimeConverterUtils.convertToLocalDateTime(result[10]):null;
            LocalDateTime shippedDate = result[11]!=null?
                LocalDateTimeConverterUtils.convertToLocalDateTime(result[11]):null;

            Invoice invoice = new Invoice();
            invoice.setShipName(result[0].toString());
            invoice.setShipAddress(result[1].toString());
            invoice.setShipCity(result[2].toString());
            invoice.setShipDistrict(result[3].toString());
            invoice.setShipWard(result[4].toString());
            invoice.setPhone(result[5].toString());
            invoice.setCustomerID(result[6].toString());
            invoice.setEmployeeID(result[7]!=null?result[7].toString():null);
            invoice.setOrderID(result[8]!=null?result[8].toString():null);
            invoice.setOrderDate(orderDate);
            invoice.setRequiredDate(requiredDate);
            invoice.setShippedDate(shippedDate);
            invoice.setShipperID((Integer) result[12]);
            invoice.setProductID((Integer) result[13]);
            invoice.setUnitPrice((BigDecimal) result[14]);
            invoice.setQuantity(((Short) result[15]).intValue());
            invoice.setExtendedPrice((BigDecimal)result[16]);
            invoice.setFreight((BigDecimal)result[17]);
            invoice.setStatus(OrderStatus.valueOf(result[18].toString()));
            invoice.setDiscountID(result[19]!=null?result[19].toString():null);
            invoice.setDiscountPercent(((Long)result[20]).intValue());
            invoice.setPaymentMethod(result[21].toString());

            return invoice;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, OrderStatus status, Integer pageNumber,
            Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByEmployeeID(employeeID={}, status={}, pageNumber={}, pageSize={})", 
            employeeID, status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDAndStatusOrderByOrderDateDesc(employeeID, status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, ShipperNotFoundException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={})", employeeID, orderID);
        Order order = orderRepository.findByEmployeeIDAndOrderID(employeeID, orderID)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return convertOrderService.convertOrderWithProducts(order);
    }

    @Override
    public Page<OrderDetails> getOrdersByCustomerID(String customerID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByCustomerID(customerID={}, pageNumber={}, pageSize={})", 
            customerID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByCustomerIDOrderByOrderDateDesc(customerID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrdersByEmployeeID(String employeeID, Integer pageNumber, Integer pageSize)
            throws JsonProcessingException, NotFoundException {
        log.info("getOrdersByEmployeeID(employeeID={}, pageNumber={}, pageSize={})", 
            employeeID, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByEmployeeIDOrderByOrderDateDesc(employeeID, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDetails> getOrders(OrderStatus status, Integer pageNumber, Integer pageSize) throws JsonProcessingException, NotFoundException {
        log.info("getOrders(status={}, pageNumber={}, pageSize={})", status, pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderDetailExtended> orders = orderDetailExtendedRepository
            .findAllByStatusOrderByOrderDateDesc(status, pageable);
        List<OrderDetails> orderDetailss = convertOrderService.convertOrders(orders.getContent());
        return new PageImpl<>(orderDetailss, pageable, orders.getTotalElements());
    }

    @Override
    public OrderWithProducts getOrderByEmployeeID(String employeeID, String orderID, OrderStatus orderStatus)
            throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, ShipperNotFoundException {
        log.info("getOrderByEmployeeID(employeeID={}, orderID={}, orderStatus={})", employeeID, orderID, orderStatus);
        Order order = orderRepository.findByOrderIDAndEmployeeIDAndStatus(orderID, employeeID, orderStatus)
            .orElseThrow(()-> new NotFoundException("Order " + orderID + " of employee "+ employeeID + " does not found"));
        return convertOrderService.convertOrderWithProducts(order);
    }


    private Long countOrderByStatus(OrderStatus status) {
        Long totalOrders = orderRepository.countOrderByStatus(status)
        .orElseThrow(()-> new RuntimeException("Error occured while counting orders"));
        return totalOrders;
    }

    @Override
    public OrderSummary getOrderSummary() {
        OrderSummary summary = new OrderSummary();
        // total pending orders
        Long totalOfPendingOrders = this.countOrderByStatus(OrderStatus.Pending);
        summary.setTotalPendingOrders(totalOfPendingOrders);
        return summary;
    }
}
