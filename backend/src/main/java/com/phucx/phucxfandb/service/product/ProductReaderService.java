package com.phucx.phucxfandb.service.product;

import com.phucx.phucxfandb.enums.ProductRatingStatus;
import com.phucx.phucxfandb.dto.request.ProductRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductReaderService {

    ProductRatingStatus getRatingStatus(String username, long productId);

    ProductDTO getProduct(long productID, Boolean isDeleted);

    Product getProductEntity(long productID);

    Page<ProductDTO> getProducts(ProductRequestParamsDTO requestParamDTO);

    Optional<Product> getRatingProductEntity(String username, long productId);

}
