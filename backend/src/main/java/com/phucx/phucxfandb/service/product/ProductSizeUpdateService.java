package com.phucx.phucxfandb.service.product;

import com.phucx.phucxfandb.dto.request.RequestProductSizeDTO;
import com.phucx.phucxfandb.dto.response.ProductSizeDTO;

public interface ProductSizeUpdateService {
    ProductSizeDTO updateProductSize(long productId, RequestProductSizeDTO requestProductSizeDTO);

}
