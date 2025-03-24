package com.phucx.phucxfoodshop.service.order.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.DiscountConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.DiscountBreifInfo;
import com.phucx.phucxfoodshop.model.DiscountDetail;
import com.phucx.phucxfoodshop.model.Invoice;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.InvoiceDetailsBuilder;
import com.phucx.phucxfoodshop.model.Order;
import com.phucx.phucxfoodshop.model.OrderDetail;
import com.phucx.phucxfoodshop.model.OrderDetailDiscount;
import com.phucx.phucxfoodshop.model.OrderDetailExtended;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderItem;
import com.phucx.phucxfoodshop.model.OrderItemDiscount;
import com.phucx.phucxfoodshop.model.OrderProduct;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.OrderWithProductsBuilder;
import com.phucx.phucxfoodshop.model.PaymentMethod;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.model.ProductWithBriefDiscount;
import com.phucx.phucxfoodshop.model.Shipper;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.repository.OrderDetailDiscountRepository;
import com.phucx.phucxfoodshop.repository.OrderDetailRepository;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.discount.DiscountService;
import com.phucx.phucxfoodshop.service.order.ConvertOrderService;
import com.phucx.phucxfoodshop.service.paymentMethod.PaymentMethodService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.shipper.ShipperService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.utils.BigDecimalUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConvertOrderServiceImp implements ConvertOrderService{
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailDiscountRepository orderDetailDiscountRepository;

    @Override
    public InvoiceDetails convertInvoiceDetails(List<Invoice> invoices
    ) throws JsonProcessingException, NotFoundException, ShipperNotFoundException, EmployeeNotFoundException {
        log.info("convertInvoiceDetails({})", invoices);
        if(invoices.isEmpty()) return null;
        // fetch invoice
        Invoice invoice = invoices.get(0);
        // fetch employee
        String salesperson = "";        
        if(invoice.getEmployeeID()!=null){
            User employee = userService.getUserByEmployeeID(invoice.getEmployeeID());
            salesperson = employee.getLastName() + " " + employee.getFirstName();
        }
        // fetch shipper
        Shipper fetchedShipper = this.shipperService.getShipperByID(invoice.getShipperID());
        // fetch products
        List<Integer> productIds = invoices.stream().map(Invoice::getProductID).collect(Collectors.toList());
        List<Product> fetchedProducts = this.productService.getProducts(productIds);
        // fetch discounts
        List<String> discountIds = invoices.stream().map(Invoice::getDiscountID).collect(Collectors.toList());
        List<DiscountDetail> fetchedDiscounts = this.discountService.getDiscountDetails(discountIds);
        // create an invoice
        InvoiceDetails invoiceDetails = new InvoiceDetailsBuilder()
            .withOrderID(invoice.getOrderID())
            .withCustomerID(invoice.getCustomerID())
            .withEmployeeID(invoice.getEmployeeID())
            .withSalesPerson(salesperson)
            .withShipName(invoice.getShipName())
            .withShipAddress(invoice.getShipAddress())
            .withShipCity(invoice.getShipCity())
            .withShipDistrict(invoice.getShipDistrict())
            .withShipWard(invoice.getShipWard())
            .withPhone(invoice.getPhone())
            .withOrderDate(invoice.getOrderDate())
            .withRequiredDate(invoice.getRequiredDate())
            .withShippedDate(invoice.getShippedDate())
            .withFreight(invoice.getFreight())
            .withStatus(invoice.getStatus())
            .withShipperName(fetchedShipper.getCompanyName())
            .withPaymentMethod(invoice.getPaymentMethod())
            .build();
        // convert invoice of customer
        List<ProductWithBriefDiscount> products = invoiceDetails.getProducts();

        for(Invoice tempInvoice: invoices){
            // find product
            Product product = findProduct(fetchedProducts, tempInvoice.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + tempInvoice.getProductID() + " does not found"));
            // add new product
            if(products.size()==0 || !products.get(products.size()-1).getProductID().equals(tempInvoice.getProductID())){
                ProductWithBriefDiscount productWithBriefDiscount = new ProductWithBriefDiscount(
                    tempInvoice.getProductID(), product.getProductName(), tempInvoice.getUnitPrice(), 
                    tempInvoice.getQuantity(), product.getPicture(), tempInvoice.getExtendedPrice());
                // set totalprice for invoice
                invoiceDetails.setTotalPrice(tempInvoice.getExtendedPrice().add(invoiceDetails.getTotalPrice()));
                products.add(productWithBriefDiscount);
            }
            // add new discount for product
            if(tempInvoice.getDiscountID()!=null && tempInvoice.getDiscountID().equalsIgnoreCase(DiscountConstant.NO_DISCOUNT.name())){
                // find discount
                DiscountDetail discount = this.findDiscount(fetchedDiscounts, tempInvoice.getDiscountID())
                    .orElseThrow(()-> new NotFoundException("Discount " + tempInvoice.getDiscountID() + " does not found"));
                DiscountBreifInfo discountBreifInfo = new DiscountBreifInfo(
                    tempInvoice.getDiscountID(), 
                    tempInvoice.getDiscountPercent(), 
                    discount.getDiscountType());
                products.get(products.size()-1).getDiscounts().add(discountBreifInfo);
                products.get(products.size()-1).setTotalDiscount(products.get(products.size()-1).getTotalDiscount() + tempInvoice.getDiscountPercent());
            }
        }
        return invoiceDetails;
    }

    @Override
    public List<OrderDetails> convertOrders(List<OrderDetailExtended> orders
    ) throws NotFoundException {
        log.info("convertOrders({})", orders);
        if(orders.isEmpty()) return new ArrayList<>();
        // fetch products
        List<Integer> productIds = orders.stream()
            .map(OrderDetailExtended::getProductID)
            .collect(Collectors.toList());
        List<Product> fetchedProducts = this.productService
            .getProducts(productIds);
        // fetch customer
        Set<String> setCustomerId = orders.stream()
            .map(OrderDetailExtended::getCustomerID)
            .collect(Collectors.toSet());
        List<String> listCustomerId = new ArrayList<>(setCustomerId);
        List<CustomerDetail> fetchedCustomers = this.customerService
            .getCustomersByIDs(listCustomerId);
        // create order
        List<OrderDetails> orderDetails = new ArrayList<>();
        for(OrderDetailExtended order: orders){
            // create a product
            // add new OrderDetails to result list when it meets a new orderID
            if(orderDetails.size()==0 || !orderDetails.get(orderDetails.size()-1).getOrderID().equals(order.getOrderID())){
                // find customer
                CustomerDetail customer = this.findCustomer(fetchedCustomers, order.getCustomerID())
                    .orElseThrow(
                        ()-> new NotFoundException("Customer " + order.getCustomerID() + " does not found")
                    );
                // create an order
                OrderDetails createdOrder = new OrderDetails();
                createdOrder.setOrderID(order.getOrderID());
                createdOrder.setStatus(order.getStatus());
                createdOrder.setFreight(order.getFreight());
                // set customer
                createdOrder.setCustomerID(customer.getCustomerID());
                createdOrder.setContactName(customer.getContactName());
                createdOrder.setPicture(customer.getPicture());
                // add order to a list of order
                orderDetails.add(createdOrder);
            }
            // add products to order
            Product product = findProduct(fetchedProducts, order.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + order.getProductID() + " does not found"));
            // add new product to the newest OrderDetail
            OrderProduct OrderProduct = new OrderProduct(order.getProductID(), product.getProductName(), 
                order.getUnitPrice(), order.getQuantity(), order.getDiscount(), order.getExtendedPrice(),
                product.getPicture());
            // set price
            orderDetails.get(orderDetails.size()-1).getProducts().add(OrderProduct);
            BigDecimal totalPrice = orderDetails.get(orderDetails.size()-1).getTotalPrice().add(order.getExtendedPrice());
            orderDetails.get(orderDetails.size()-1).setTotalPrice(BigDecimalUtils.formatter(totalPrice));
        }
        return orderDetails;
    }

    @Override
    public OrderDetails convertOrderDetail(List<OrderDetailExtended> orderDetailExtendeds)
            throws JsonProcessingException, NotFoundException, CustomerNotFoundException {
        log.info("convertOrderDetail({})", orderDetailExtendeds);
        if(orderDetailExtendeds.isEmpty()) return null;
        // get the first element inside orderproducts
        OrderDetailExtended firstElement = orderDetailExtendeds.get(0);
        // get customer
        String customerID = firstElement.getCustomerID();
        CustomerDetail fetchedCustomer = customerService
            .getCustomerByID(customerID);
        // get products
        List<Integer> productIds = orderDetailExtendeds.stream()
            .map(OrderDetailExtended::getProductID)
            .collect(Collectors.toList());
        List<Product> fetchedProducts = productService
            .getProducts(productIds);
        // create an orderdetails instance
        OrderDetails order = new OrderDetails();
        order.setOrderID(firstElement.getOrderID());
        order.setStatus(firstElement.getStatus());
        order.setFreight(firstElement.getFreight());
        // set employee of order
        order.setEmployeeID(firstElement.getEmployeeID());
        // set customer of order
        order.setCustomerID(fetchedCustomer.getCustomerID());
        order.setContactName(fetchedCustomer.getContactName());
        order.setPicture(fetchedCustomer.getPicture());
        // set product for order
        for(OrderDetailExtended orderDetailExtended: orderDetailExtendeds){
            Product product = this.findProduct(fetchedProducts, orderDetailExtended.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + orderDetailExtended.getProductID() 
                    + " does not found"));
            // add new product to the newest OrderDetail
            OrderProduct orderProduct = new OrderProduct(
                orderDetailExtended.getProductID(), 
                product.getProductName(), 
                orderDetailExtended.getUnitPrice(), 
                orderDetailExtended.getQuantity(), 
                orderDetailExtended.getDiscount(), 
                orderDetailExtended.getExtendedPrice(),
                product.getPicture());
            // add products to order
            order.getProducts().add(orderProduct);
            // set order totalPrice
            order.setTotalPrice(order.getTotalPrice().add(orderDetailExtended.getExtendedPrice()));
        }
        return order;
    }

    @Override
    public OrderWithProducts convertOrderWithProducts(Order order) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, ShipperNotFoundException {
        log.info("convertOrderWithProducts({})", order);
        // fetch infomation
        // get customer
        String customerID = order.getCustomerID();
        CustomerDetail fetchedCustomer = customerService.getCustomerByID(customerID);
        // get employee
        String salePerson = "";
        String employeeID = order.getEmployeeID();
        if(employeeID!=null){
            User user = userService.getUserByEmployeeID(employeeID);
            salePerson = user.getLastName() + " " + user.getFirstName();
        }
        // get shipper
        Integer shipperID = order.getShipVia();
        Shipper fetchedShipper = shipperService.getShipperByID(shipperID);
        // get payment method
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByOrderID(order.getOrderID());
        // get products
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderID(order.getOrderID());
        List<Integer> productIds = orderDetails.stream().map(OrderDetail::getProductID).collect(Collectors.toList());
        List<Product> products = productService.getProducts(productIds);
        // create and order
        OrderWithProducts orderWithProducts = new OrderWithProductsBuilder()
            .withOrderID(order.getOrderID())
            .withCustomerID(customerID)
            .withContactName(fetchedCustomer.getContactName())
            .withEmployeeID(employeeID)
            .withSalesPerson(salePerson)
            .withOrderDate(order.getOrderDate())
            .withRequiredDate(order.getRequiredDate())
            .withShippedDate(order.getShippedDate())
            .withShipVia(shipperID)
            .withShipperName(fetchedShipper.getCompanyName())
            .withShipperPhone(fetchedShipper.getPhone())
            .withFreight(order.getFreight())
            .withShipName(order.getShipName())
            .withShipAddress(order.getShipAddress())
            .withShipCity(order.getShipCity())
            .withShipDistrict(order.getShipDistrict())
            .withShipWard(order.getShipWard())
            .withPhone(order.getPhone())
            .withStatus(order.getStatus())
            .withMethod(paymentMethod.getMethodName())
            .build();
        for(OrderDetail orderDetail: orderDetails){
            // find product
            Product product = this.findProduct(products, orderDetail.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + orderDetail.getProductID() + " does not found"));
            // create a product for order
            OrderItem orderItem = new OrderItem(orderDetail.getProductID(), product.getProductName(),
                orderDetail.getQuantity(), product.getPicture(), orderDetail.getUnitPrice());
            Integer totalDiscount = Integer.valueOf(0);
            // fetch product'discounts
            String orderID = orderDetail.getOrderID();
            Integer productID = orderDetail.getProductID();
            List<OrderDetailDiscount> orderDetailDiscounts = orderDetailDiscountRepository
                .findByOrderIDAndProductID(orderID, productID);
            // get discounts of product
            List<String> discountIds = orderDetailDiscounts.stream().map(OrderDetailDiscount::getDiscountID).collect(Collectors.toList());
            List<DiscountDetail> fetchedDiscounts = discountService.getDiscountDetails(discountIds);
            // add discount
            List<OrderItemDiscount> discounts = new ArrayList<>();
            for(OrderDetailDiscount orderDetailDiscount: orderDetailDiscounts){
                // find discount
                DiscountDetail fetchedDiscount = this.findDiscount(fetchedDiscounts, orderDetailDiscount.getDiscountID())
                    .orElseThrow(()-> new NotFoundException("Discount "+orderDetailDiscount.getDiscountID() + " does not found"));
                OrderItemDiscount discount = new OrderItemDiscount(
                    orderDetailDiscount.getDiscountID(), 
                    orderDetailDiscount.getAppliedDate(), 
                    orderDetailDiscount.getDiscountPercent(), 
                    fetchedDiscount.getDiscountType());
                // add total discounts
                totalDiscount +=orderDetailDiscount.getDiscountPercent();
                discounts.add(discount);
            }
  
            // add discounts and price of each product
            orderItem.setDiscounts(discounts);
            BigDecimal productDiscount = BigDecimal.valueOf(1- Double.valueOf(totalDiscount) /100);
            
            BigDecimal extendedPrice = BigDecimal.valueOf(orderDetail.getQuantity())
                .multiply(orderDetail.getUnitPrice())
                .multiply(productDiscount);
            extendedPrice = BigDecimalUtils.formatter(extendedPrice);

            orderWithProducts.setTotalPrice(orderWithProducts.getTotalPrice().add(extendedPrice));
            // log.info("totalPrice {}", totalPrice);
            orderItem.setExtendedPrice(extendedPrice);
            // add product to order
            orderWithProducts.getProducts().add(orderItem);
        }
        return orderWithProducts;
    }

    // find product
    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }
    // find discount
    private Optional<DiscountDetail> findDiscount(List<DiscountDetail> discounts, String discountID){
        return discounts.stream().filter(p -> p.getDiscountID().equalsIgnoreCase(discountID)).findFirst();
    }
    // find customer
    private Optional<CustomerDetail> findCustomer(List<CustomerDetail> customers, String customerID){
        return customers.stream().filter(p -> p.getCustomerID().equalsIgnoreCase(customerID)).findFirst();
    }    
}
