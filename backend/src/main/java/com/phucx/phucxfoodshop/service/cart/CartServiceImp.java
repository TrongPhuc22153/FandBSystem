package com.phucx.phucxfoodshop.service.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.InsufficientResourcesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.phucxfoodshop.config.ServerURLProperties;
import com.phucx.phucxfoodshop.constant.CookieConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CartOrderInfo;
import com.phucx.phucxfoodshop.model.CartProduct;
import com.phucx.phucxfoodshop.model.CartProductInfo;
import com.phucx.phucxfoodshop.model.CartProductsCookie;
import com.phucx.phucxfoodshop.model.CurrentProduct;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.OrderItem;
import com.phucx.phucxfoodshop.model.OrderItemDiscount;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.OrderWithProductsBuilder;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.utils.BigDecimalUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImp implements CartService{
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServerURLProperties server;

    @Override
    public CartOrderInfo updateCartCookie(String encodedCartJson, List<CartProduct> products, HttpServletResponse response) 
        throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        log.info("updateCartCookie(encodedCartJson={}, orderItem={})", encodedCartJson, products);
        if(products==null || products.isEmpty()){ throw new NotFoundException("Product does not found");}
        // get existed cart from json format
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
        }
        // fetch product 
        List<Integer> productIDs = products.stream().map(CartProduct::getProductID).collect(Collectors.toList());
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);

        for (CurrentProduct currentProduct : fetchedProducts) {
            CartProduct cartProduct = this.findProduct(products, currentProduct.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + currentProduct.getProductID() + " name " + currentProduct.getProductName() + " does not found"));
            // check product's quantity with product's inStocks
            if(currentProduct.getUnitsInStock()<cartProduct.getQuantity())
                throw new InsufficientResourcesException("Product " + currentProduct.getProductName() + " exceeds available stock");
            
            // check whether the product exists in cart or not
            boolean isExisted = false;
            for(CartProduct item: items){
                if(item.getProductID().equals(cartProduct.getProductID())){
                    item.setQuantity(cartProduct.getQuantity());
                    item.setIsSelected(cartProduct.getIsSelected());
                    isExisted = true;
                    break;
                }
            }
            // product does not exist in cart
            if(!isExisted){
                items.add(cartProduct);
            }
        }
        // write cart as json format
        String updatedCartJson = objectMapper.writeValueAsString(items);
        // update cookie
        Cookie cookie = this.createCookie(updatedCartJson);
        response.addCookie(cookie);
        return this.createCartOrder(items);
    }

    private Optional<CartProduct> findProduct(List<CartProduct> products, Integer productID){
        return products.stream().filter(product -> product.getProductID().equals(productID)).findFirst();
    }

    // create a new cookie
    private Cookie createCookie(String cartJson){
        String encodedData = this.encodeCookie(cartJson);
        Cookie cookie = new Cookie(CookieConstant.CART_COOKIE, encodedData);
        cookie.setPath(CookieConstant.PATH_COOKIE);
        cookie.setMaxAge(CookieConstant.MAX_AGE);
        cookie.setDomain(server.getDomain());
        return cookie;
    }
    
    @Override
    public CartOrderInfo removeProduct(Integer productID, String encodedcartJson, HttpServletResponse response) 
         throws JsonProcessingException, NotFoundException {
        if(productID!=null){
            TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
            List<CartProduct> items = new ArrayList<>();
            if(encodedcartJson!=null){
                String cartJson = this.decodeCookie(encodedcartJson);
                items = objectMapper.readValue(cartJson, typeRef);
                List<CartProduct> orderItems = items.stream()
                    .filter(item -> item.getProductID()!=productID)
                    .collect(Collectors.toList());
                // convert into json format
                String updatedCartJson = objectMapper.writeValueAsString(orderItems);
                // update cookie
                Cookie cookie = this.createCookie(updatedCartJson);
                response.addCookie(cookie);
                return this.createCartOrder(orderItems);
            }
        }
        return this.getCartOrder(encodedcartJson);
    }

    // decode from base64
    private String decodeCookie(String cookie){
        byte[] cookieDecoded = Base64.getDecoder().decode(cookie);
        return new String(cookieDecoded);
    }
    // encode to base64
    private String encodeCookie(String cookie){
        return Base64.getEncoder().encodeToString(cookie.getBytes());
    }
    // create an order from cart items 
    private OrderWithProducts createOrderDetail(List<CartProduct> products) throws NotFoundException{   
        log.info("createOrderDetail({})", products);
        List<Integer> productIDs = products.stream()
            .map(CartProduct::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);
        // convert 
        OrderWithProducts order = new OrderWithProductsBuilder().build();
        for (CartProduct product: products) {
            // find product
            CurrentProduct fetchedProduct = this.findCurrentProduct(fetchedProducts, product.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + product.getProductID() + " does not found"));
            // add product to order
            // add product to cart
            OrderItem item = new OrderItem(product.getProductID(), fetchedProduct.getProductName(),
                fetchedProduct.getCategoryName(), product.getQuantity(), fetchedProduct.getUnitsInStock(), 
                fetchedProduct.getPicture(), fetchedProduct.getUnitPrice());
                
            // add discount
            OrderItemDiscount discount = new OrderItemDiscount();
            discount.setDiscountID(fetchedProduct.getDiscountID());
            discount.setDiscountPercent(fetchedProduct.getDiscountPercent());
            item.getDiscounts().add(discount);

            // add total discount to product
            item.setTotalDiscount(discount.getDiscountPercent());

            // calculate extended price
            Double productDiscount = 1- Double.valueOf(fetchedProduct.getDiscountPercent())/100;
            BigDecimal price = BigDecimal.valueOf(product.getQuantity()).multiply(item.getUnitPrice());
            BigDecimal extendedPrice = price.multiply(BigDecimal.valueOf(productDiscount));
            // log.info("discount: {}", (extendedPrice));
            item.setExtendedPrice(BigDecimalUtils.formatter(extendedPrice));

            // add product
            order.getProducts().add(item);
            // calculate total price of order 
            order.setTotalPrice(BigDecimalUtils.formatter(order.getTotalPrice().add(extendedPrice)));
        }
        return order;
    }

    // find products
    private Optional<CurrentProduct> findCurrentProduct(List<CurrentProduct> products, Integer productID){
        return products.stream().filter(product-> product.getProductID().equals(productID)).findFirst();
    }

    @Override
    public List<CartProduct> getListProducts(String encodedCartJson) throws JsonProcessingException {
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
            List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);
            return listProducts;
        }   
        return new ArrayList<>();
    }
    @Override
    public CartProductsCookie getNumberOfProducts(String encodedCartJson) throws JsonProcessingException {
        int numberOfCartItems = this.getListProducts(encodedCartJson).size();
        CartProductsCookie cartProductsCookie = new CartProductsCookie(numberOfCartItems);
        return cartProductsCookie;
    }
    @Override
    public OrderWithProducts getOrder(String encodedCartJson, String username) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException, CustomerNotFoundException {
        log.info("getOrder(encodedCartJson={}, username={})", encodedCartJson, username);
        User user = userService.getUser(username);
        String userID = user.getUserID();
        CustomerDetail customer = this.customerService.getCustomerByUserID(userID);
        // create an order
        OrderWithProducts order = this.getPurchaseOrder(encodedCartJson);
        order.setShipName(customer.getContactName());
        order.setShipAddress(customer.getAddress());
        order.setShipCity(customer.getCity());
        order.setShipDistrict(customer.getDistrict());
        order.setShipWard(customer.getWard());
        order.setPhone(customer.getPhone());
        log.info("orderDetails: {}", order);
        return order;
    }

    // get order in cart
    private OrderWithProducts getPurchaseOrder(String encodedCartJson) throws JsonProcessingException, EmptyCartException, InvalidOrderException, NotFoundException{
        if(encodedCartJson==null){
            throw new EmptyCartException("Your cart does not have any products");
        } 
        String cartJson = this.decodeCookie(encodedCartJson);
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);
        // fitler selected products
        listProducts = listProducts.stream().filter(CartProduct::getIsSelected).collect(Collectors.toList());
        // create an order
        OrderWithProducts order = this.createOrderDetail(listProducts);
        if(order==null || order.getProducts().isEmpty()){
            throw new InvalidOrderException("Your order does not contain any products");
        }
        return order;
    }


    // return order including ship address, name, phone and products of order in cart
    @Override
    public CartOrderInfo getCartProducts(String encodedCartJson) throws JsonProcessingException, NotFoundException {
        log.info("getCartProducts(encodedCartJson={})", encodedCartJson);
        return this.getCartOrder(encodedCartJson);
    }



    // get order in cart
    private CartOrderInfo getCartOrder(String encodedCartJson) throws JsonProcessingException, NotFoundException{
        if(encodedCartJson==null){
            return new CartOrderInfo();
        }   
        String cartJson = this.decodeCookie(encodedCartJson);
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> listProducts = objectMapper.readValue(cartJson, typeRef);

        // create an order
        return this.createCartOrder(listProducts);
    }

    private CartOrderInfo createCartOrder(List<CartProduct> products) throws NotFoundException{
        log.info("createCartOrder({})", products);
        if(products==null || products.isEmpty()) return new CartOrderInfo();
        // extract productIDs from products
        List<Integer> productIDs = products.stream()
            .map(CartProduct::getProductID)
            .collect(Collectors.toList());
        // fetch products from database
        List<CurrentProduct> fetchedProducts = this.productService.getCurrentProducts(productIDs);
        // convert 
        CartOrderInfo order = new CartOrderInfo();
        for (CartProduct product: products) {
            // find product
            CurrentProduct fetchedProduct = this.findCurrentProduct(fetchedProducts, product.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + product.getProductID() + " does not found"));
            // add product to order
            CartProductInfo item = new CartProductInfo(product.getProductID(), fetchedProduct.getProductName(),
                fetchedProduct.getCategoryName(), product.getQuantity(), fetchedProduct.getUnitsInStock(), 
                fetchedProduct.getPicture(), fetchedProduct.getUnitPrice(), product.getIsSelected());     
            // add discount
            OrderItemDiscount discount = new OrderItemDiscount();
            discount.setDiscountID(fetchedProduct.getDiscountID());
            discount.setDiscountPercent(fetchedProduct.getDiscountPercent());
            item.getDiscounts().add(discount);
            // add total discount to product
            item.setTotalDiscount(discount.getDiscountPercent());
            // calculate extended price
            Double productDiscount = 1- Double.valueOf(fetchedProduct.getDiscountPercent())/100;
            BigDecimal price = BigDecimal.valueOf(product.getQuantity()).multiply(item.getUnitPrice());
            BigDecimal extendedPrice = price.multiply(BigDecimal.valueOf(productDiscount));
            
            item.setExtendedPrice(BigDecimalUtils.formatter(extendedPrice));

            // add product
            order.getProducts().add(item);
            // calculate total price of order 
            if(product.getIsSelected()){
                order.setTotalPrice(BigDecimalUtils.formatter(order.getTotalPrice().add(extendedPrice)));
            }
        }
        return order;
    }

    @Override
    public CartOrderInfo removeProducts(HttpServletResponse response) throws JsonProcessingException {
        log.info("removeProducts()");
        Cookie cookie = this.createCookie("[]");
        response.addCookie(cookie);
        return new CartOrderInfo();
    }

    @Override
    public CartOrderInfo addProduct(String encodedCartJson, List<CartProduct> cartProducts,
            HttpServletResponse response) throws JsonProcessingException, InsufficientResourcesException, NotFoundException {
        log.info("addProduct(encodedCartJson={}, cartProducts={})", encodedCartJson, cartProducts);
        if(cartProducts==null || cartProducts.isEmpty()){ 
            throw new NotFoundException("Product does not found");
        }
        // get existed cart from json format
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
        }
        // fetch product 
        List<Integer> productIDs = cartProducts.stream()
            .map(CartProduct::getProductID)
            .collect(Collectors.toList());
        List<CurrentProduct> fetchedProducts = this.productService
            .getCurrentProducts(productIDs);

        for (CurrentProduct currentProduct : fetchedProducts) {
            CartProduct cartProduct = this.findProduct(cartProducts, currentProduct.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + currentProduct.getProductID() + 
                    " name " + currentProduct.getProductName() + " does not found"));
            Optional<CartProduct> itemInCart = this.findProduct(items, currentProduct.getProductID());
            Integer quantityInCart = itemInCart.isPresent()?itemInCart.get().getQuantity():0;
            // check product's quantity with product's inStocks
            if(currentProduct.getUnitsInStock()<cartProduct.getQuantity()+quantityInCart)
                throw new InsufficientResourcesException("Product " + currentProduct.getProductName() + 
                    " exceeds available stock");
            
            // check whether the product exists in cart or not
            boolean isExisted = false;
            for(CartProduct item: items){
                if(item.getProductID().equals(cartProduct.getProductID())){
                    item.setQuantity(cartProduct.getQuantity() + item.getQuantity());
                    item.setIsSelected(cartProduct.getIsSelected());
                    isExisted = true;
                    break;
                }
            }
            // product does not exist in cart
            if(!isExisted){
                items.add(cartProduct);
            }
        }
        // write cart as json format
        String updatedCartJson = objectMapper.writeValueAsString(items);
        // update cookie
        Cookie cookie = this.createCookie(updatedCartJson);
        response.addCookie(cookie);
        return this.createCartOrder(items);

    }

    @Override
    public CartOrderInfo removeProducts(List<Integer> productIDs, String encodedCartJson, HttpServletResponse response)
            throws JsonProcessingException, NotFoundException {
        log.info("removeProducts(productIDs={}, encodedCartJson={}", productIDs, encodedCartJson);
        
        TypeReference<List<CartProduct>> typeRef = new TypeReference<List<CartProduct>>() {};
        List<CartProduct> items = new ArrayList<>();
        if(encodedCartJson!=null){
            String cartJson = this.decodeCookie(encodedCartJson);
            items = objectMapper.readValue(cartJson, typeRef);
            // filter products
            List<CartProduct> orderItems = items.stream()
                .filter(item -> !productIDs.contains(item.getProductID()))
                .collect(Collectors.toList());
            // convert into json format
            String updatedCartJson = objectMapper.writeValueAsString(orderItems);
            // update cookie
            Cookie cookie = this.createCookie(updatedCartJson);
            response.addCookie(cookie);
            return this.createCartOrder(orderItems);
        }

        return this.getCartOrder(encodedCartJson);
    }
}
