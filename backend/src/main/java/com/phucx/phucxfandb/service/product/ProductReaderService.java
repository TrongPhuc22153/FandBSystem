package com.phucx.phucxfandb.service.product;

import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductReaderService {
    ProductDTO getProduct(long productID);
    Product getProductEntity(long productID);
    Page<ProductDTO> getProducts(String field, Sort.Direction direction, int pageNumber, int pageSize);
    Page<ProductDTO> getProductsByName(String productName, String field, Sort.Direction direction, int pageNumber, int pageSize);
    Page<ProductDTO> getProductsBySearch(String searchValue, String field, Sort.Direction direction, int pageNumber, int pageSize);
    Page<ProductDTO> getProductsByCategoryId(long categoryId, String field, Sort.Direction direction, int pageNumber, int pageSize);
    Page<ProductDTO> getProductsByCategory(String categoryName, String field, Sort.Direction direction, int pageNumber, int pageSize);
    Page<ProductDTO> getFeaturedProducts( String field, Sort.Direction direction, int pageNumber, int pageSize);
}
