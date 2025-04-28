package com.phucx.phucxfandb.service.product;

import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Product;

import java.util.List;

public interface ProductUpdateService {
    Product updateProductInStock(long productId, int quantity);
    ProductDTO updateProduct(long productId, RequestProductDTO requestProductDTO);
    ProductDTO createProduct(RequestProductDTO requestProductDTO);
    List<ProductDTO> createProducts(List<RequestProductDTO> requestProductDTOs);
}
