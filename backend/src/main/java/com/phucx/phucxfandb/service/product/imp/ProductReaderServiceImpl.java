package com.phucx.phucxfandb.service.product.imp;

import com.phucx.phucxfandb.dto.request.ProductRequestParamDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ProductMapper;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReaderServiceImpl implements ProductReaderService {
    private final ProductRepository productRepository;
    private final ImageReaderService imageReaderService;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProduct(long productId, Boolean isDeleted) {
        log.info("getProduct(productId={}, isDeleted={})", productId, isDeleted);
        Optional<Product> product;
        if(isDeleted==null){
            product = productRepository.findById(productId);
        }else{
            product = productRepository.findByProductIdAndIsDeleted(productId, isDeleted);
        }
        return product.map(this::setImageUrl)
                .map(mapper::toProductDTO)
                .orElseThrow(()-> new NotFoundException("Product", "id", String.valueOf(productId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntity(long productID) {
        log.info("getProductEntity(productID={})", productID);
        return productRepository.findByProductIdAndIsDeletedFalse(productID)
                .orElseThrow(() -> new NotFoundException("Product", productID));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(ProductRequestParamDTO requestParamDTO) {
        Pageable pageable = PageRequest.of(
                requestParamDTO.getPage(),
                requestParamDTO.getSize(),
                Sort.by(requestParamDTO.getDirection(), requestParamDTO.getField())
        );

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasCategoryId(requestParamDTO.getCategoryId()))
                .and(ProductSpecification.hasIsDeleted(requestParamDTO.getIsDeleted()))
                .and(ProductSpecification.isFeatured(requestParamDTO.getIsFeatured()))
                .and(ProductSpecification.hasSearchValue(requestParamDTO.getSearchValue()));

        return productRepository.findAll(spec, pageable)
                .map(this::setImageUrl)
                .map(mapper::toProductDTO);
    }

    private Product setImageUrl(Product product){
        if(!(product.getPicture()==null || product.getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(product.getPicture());
            product.setPicture(imageUrl);
        }
        return product;
    }
}
