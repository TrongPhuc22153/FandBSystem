package com.phucx.phucxfoodshop.service.product.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.constant.ProductStatus;
import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.InSufficientInventoryException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Category;
import com.phucx.phucxfoodshop.model.CurrentProduct;
import com.phucx.phucxfoodshop.model.ExistedProduct;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.model.ProductDetail;
import com.phucx.phucxfoodshop.model.ProductDetails;
import com.phucx.phucxfoodshop.model.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.ProductSize;
import com.phucx.phucxfoodshop.model.ProductStockTableType;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.model.SellingProduct;
import com.phucx.phucxfoodshop.repository.CurrentProductRepository;
import com.phucx.phucxfoodshop.repository.ExistedProductRepository;
import com.phucx.phucxfoodshop.repository.ProductDetailRepository;
import com.phucx.phucxfoodshop.repository.ProductRepository;
import com.phucx.phucxfoodshop.service.category.CategoryService;
import com.phucx.phucxfoodshop.service.discount.ValidateDiscountService;
import com.phucx.phucxfoodshop.service.image.ProductImageService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.product.ProductSizeService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSizeService productSizeService;
    @Autowired
    private ExistedProductRepository existedProductRepository;
    @Autowired
    private CurrentProductRepository currentProductRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ValidateDiscountService validateDiscountService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> getProducts() {
        log.info("getProducts()");
        List<Product> products = productRepository.findAll();
        return productImageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProducts(int pageNumber, int pageSize) {
        log.info("getProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        
        Page<Product> productsPageable = productRepository.findAll(page);
        productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Product getProduct(int productID) throws NotFoundException {
        log.info("getProduct(productID={}", productID);
        Product product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        this.productImageService.setProductImage(product);
        return product;
    }

    @Override
    public List<Product> getProducts(String productName) {
        log.info("getProducts(productName={}", productName);
        List<Product> products = productRepository.findByProductName(productName);
        return this.productImageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName) {
        log.info("getProductsByName(productName={}, pageNumber={}, pageSize={}", productName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByProductName(productName, page);
        this.productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName) {
        log.info("getProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={}", categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByCategoryNameLike(categoryName, page);
        this.productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public CurrentProduct getCurrentProduct(int productID) throws NotFoundException {
        log.info("getCurrentProduct(productID={})", productID);
        CurrentProduct product = currentProductRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return this.productImageService.setCurrentProductImage(product);
    }

    

    @Override
    public List<CurrentProduct> getCurrentProduct() {
        log.info("getCurrentProduct()");
        List<CurrentProduct> products = currentProductRepository.findAll();
        return this.productImageService.setCurrentProductsImage(products);
    }

    @Override
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize) {
        log.info("getCurrentProduct(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findAll(pageable);
        productImageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize) throws NotFoundException {
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
        log.info("getCurrentProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={})", categoryName, pageNumber, pageSize);
        // get category
        List<Category> categories = categoryService.getCategoryLike(categoryName);
        if(categories==null || categories.isEmpty()) 
            throw new NotFoundException("Category " + categoryName + " does not found");
        Category fetchedCategory = categories.get(0);
        // get products based on category name
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findByCategoryName(fetchedCategory.getCategoryName(), page);
        productImageService.setCurrentProductsImage(products.getContent());
        return products;
    }

    @Override
    public ProductDetail getProductDetail(int productID) throws NotFoundException {
        log.info("getProductDetail(productID={})", productID);
        ProductDetail product = productDetailRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return productImageService.setProductDetailImage(product);
    }

    @Override
    public ProductDetail updateProductDetail(ProductDetail productDetail) throws NotFoundException {  
        log.info("updateProductDetail()", productDetail.toString());
        if(productDetail.getProductID()==null) throw new NotFoundException("Product Id is null");
        Integer productID = productDetail.getProductID();
        ProductDetail fetchedProduct = productDetailRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        // extract image's name from url
        String picture = ImageUtils.getImageName(productDetail.getPicture());
        // update product detail
        Boolean result = productDetailRepository.updateProduct(
            fetchedProduct.getProductID(), productDetail.getProductName(), 
            productDetail.getQuantityPerUnit(), productDetail.getUnitPrice(), 
            productDetail.getUnitsInStock(), productDetail.getDiscontinued(), 
            picture, productDetail.getDescription(), productDetail.getCategoryID());

        if(!result) throw new RuntimeException("Product " + productID + " can not be updated");

        productDetail.setPicture(picture);
        productImageService.setProductDetailImage(productDetail);
        return productDetail;
        
    }
    @Override
    public Boolean insertProductDetail(ProductDetail productDetail) throws EntityExistsException {
        log.info("insertProductDetail({})", productDetail);
        List<Product> products = productRepository.findByProductName(productDetail.getProductName());
        if(!products.isEmpty()){
            throw new EntityExistsException("Product " + productDetail.getProductName() + " already exists");
        }
        // extract image's name from url
        String picture = ImageUtils.getImageName(productDetail.getPicture());
        // add new product
        Boolean result = productDetailRepository.insertProduct(
            productDetail.getProductName(), productDetail.getQuantityPerUnit(), 
            productDetail.getUnitPrice(), productDetail.getUnitsInStock(), 
            productDetail.getDiscontinued(), picture, 
            productDetail.getDescription(), productDetail.getCategoryID());
        return result;
    }

    @Override
    public List<Product> getProducts(List<Integer> productIDs) {
        log.info("getProducts(productIds={})", productIDs);
        List<Product> products = productRepository.findAllById(productIDs);
        this.productImageService.setProductsImage(products);
        return products;
    }

    @Override
    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs) {
        log.info("getCurrentProducts(productIDs={})", productIDs);
        List<CurrentProduct> products = this.currentProductRepository.findAllById(productIDs);
        this.productImageService.setCurrentProductsImage(products);
        return products;
    }

    private Boolean updateProductsInStock(List<ProductStockTableType> productStocks) {
        log.info("updateProductsInStock(productStocks={})", productStocks);
        Boolean result = true;
        for (ProductStockTableType productStock : productStocks) {
            Boolean status = productRepository.updateProductUnitsInStock(
                productStock.getProductID(), 
                productStock.getUnitsInStock());
            result = result && status;
        }
        return result;
    }

    @Override
    public ResponseFormat validateAndProcessProducts(List<ProductDiscountsDTO> products) {
        log.info("validateAndProcessProducts({})", products);
        ResponseFormat responseFormat = new ResponseFormat();
        try {
            List<ProductStockTableType> productStocks = new ArrayList<>();
            // fetch products
            // extract productID
            List<Integer> productIDs = products.stream()
                .map(ProductDiscountsDTO::getProductID)
                .collect(Collectors.toList());
            // get products
            List<Product> fetchedProducts = productRepository
                .findAllById(productIDs);    
            // validate discounts
            ResponseFormat isValidDiscounts = validateDiscountService
                .validateDiscountsOfProducts(products);
            if(!isValidDiscounts.getStatus()) 
                throw new InvalidDiscountException(isValidDiscounts.getError());
            
            // validate and update product inStock with order product quantity
            for(ProductDiscountsDTO product : products){
                // get product
                Product fetchedProduct = findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> 
                        new NotFoundException("Product "+product.getProductID()+" does not found")
                    );
                // check whether the product is discontinued or not?
                if(fetchedProduct.getDiscontinued().equals(ProductStatus.Discontinued.getStatus()))
                    throw new RuntimeException("Product " + fetchedProduct.getProductName() + " is discontinued");
                // validate product's stock
                int orderQuantity = product.getQuantity();
                int inStocks = fetchedProduct.getUnitsInStock();
                if(orderQuantity>inStocks){
                    throw new InSufficientInventoryException(
                        "Product " + fetchedProduct.getProductName() + 
                        " does not have enough stocks in inventory");
                }
                // add product new in stock
                ProductStockTableType newProductStock = new ProductStockTableType();
                newProductStock.setProductID(product.getProductID());
                newProductStock.setUnitsInStock(inStocks-orderQuantity);
                productStocks.add(newProductStock);
            }
            // update product's instocks
            Boolean isUpdated = updateProductsInStock(productStocks);
            if(!isUpdated) throw new RuntimeException("Can not update product in stocks");
            responseFormat.setStatus(true);
            return responseFormat;
        } catch (NotFoundException | InvalidDiscountException | RuntimeException | InSufficientInventoryException e) {
            log.warn("Error: {}", e.getMessage());
            responseFormat.setStatus(false);
            responseFormat.setError(e.getMessage());
            return responseFormat;
        }
    }

    // find product
    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(p -> p.getProductID().equals(productID)).findFirst();
    }

    @Override
    public Boolean updateProductInStock(List<ProductStockTableType> products) throws NotFoundException {
        log.info("updateProductInStock({})", products);
        // fetch products
        List<Integer> productIDs = products.stream()
            .map(ProductStockTableType::getProductID)
            .collect(Collectors.toList());

        List<Product> fetchedProducts = productRepository.findAllById(productIDs);

        for (ProductStockTableType product : products) {
            Product fetchedProduct = this.findProduct(fetchedProducts, product.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + product.getProductID() + " does not found!"));
            product.setUnitsInStock(product.getUnitsInStock()+fetchedProduct.getUnitsInStock());
        }
        // update product instock
        Boolean status = this.updateProductsInStock(products);
        return status;
    }

    @Override
    public Page<ExistedProduct> getExistedProducts(int pageNumber, int pageSize) {
        log.info("getExistedProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ExistedProduct> products = existedProductRepository.findAll(pageable);
        productImageService.setExistedProductsImage(products.getContent());
        return products;
    }

    @Override
    public ResponseFormat validateProducts(List<ProductDiscountsDTO> products) {
        log.info("validateProducts({})", products);
        ResponseFormat responseFormat = new ResponseFormat();
        try {
            // fetch products
            // extract productID
            List<Integer> productIDs = products.stream()
                .map(ProductDiscountsDTO::getProductID)
                .collect(Collectors.toList());
            // get products
            List<Product> fetchedProducts = productRepository.findAllById(productIDs);    
            // validate discounts
            List<String> discountIds = products.stream()
                .flatMap(product -> product.getDiscountIDs().stream())
                .collect(Collectors.toList());
            if(discountIds!=null && !discountIds.isEmpty()){
                ResponseFormat isValidDiscounts = validateDiscountService
                    .validateDiscountsOfProducts(products);
                if(!isValidDiscounts.getStatus()) 
                    throw new InvalidDiscountException(isValidDiscounts.getError());
            }
            // validate products
            for(ProductDiscountsDTO product : products){
                // get product
                Product fetchedProduct = findProduct(fetchedProducts, product.getProductID())
                    .orElseThrow(()-> 
                        new NotFoundException("Product "+product.getProductID()+" does not found")
                    );
                // check customer product quantity
                if(fetchedProduct.getUnitsInStock()<product.getQuantity()){
                    throw new RuntimeException("Product " + fetchedProduct.getProductName() + " exceeds available stock!");
                }
                // check whether the product is discontinued or not?
                if(fetchedProduct.getDiscontinued().equals(ProductStatus.Discontinued.getStatus())){
                    throw new RuntimeException("Product " + fetchedProduct.getProductName() + " is discontinued");
                }
            }
            responseFormat.setStatus(true);

        } catch (RuntimeException | NotFoundException | InvalidDiscountException e) {
            log.error("Error: {}", e.getMessage());
            responseFormat.setStatus(false);
            responseFormat.setError(e.getMessage());
        }
        return responseFormat;
    }

    @Override
    public Boolean updateProductsInStocks(List<ProductStockTableType> productStocks) throws NotFoundException {
        log.info("updateProductsInStocks({})", productStocks);
        Boolean status = this.updateProductInStock(productStocks);
        return status;
    }

    @Override
    public ProductDetails getProductDetails(int productID) throws NotFoundException {
        log.info("getProductDetails(productID={})", productID);
        ProductDetail productDetail = this.getProductDetail(productID);
        ProductSize productSize = productSizeService.getProductSize(productID);
        return new ProductDetails(productDetail, productSize);
    }

    @Override
    public List<SellingProduct> getTopSellingProducts(Integer year, Integer limit) {
        log.info("getTopSellingProducts(year={}, limit={})", year, limit);
        StoredProcedureQuery procedureQuery = entityManager
            .createStoredProcedureQuery("GetTopSellingProducts")
            .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
            .setParameter(1, year)
            .setParameter(2, PaymentStatusConstant.SUCCESSFUL.name().toLowerCase())
            .setParameter(3, limit);
        List<Object[]> results = procedureQuery.getResultList();


        return results.stream().map(result -> {
            String pictureUri = productImageService
                .getPictureUri(result[8].toString());
            String description = result[9]!=null?result[9].toString():null;
            String quantityPerunit = result[4]!=null?result[4].toString():null;

            SellingProduct product = new SellingProduct();
            product.setQuantity(((BigDecimal) result[0]).intValue());
            product.setProductID((Integer) result[1]);
            product.setProductName(result[2].toString());
            product.setCategoryID((Integer)result[3]);
            product.setQuantityPerUnit(quantityPerunit);
            product.setUnitPrice((BigDecimal)result[5]);
            product.setUnitsInStock(((Short) result[6]).intValue());
            product.setDiscontinued((Boolean)result[7]);
            product.setPicture(pictureUri);
            product.setDescription(description);
            
            return product;
        }).collect(Collectors.toList());
    }

}
