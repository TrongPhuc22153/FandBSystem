package com.phucx.phucxfandb.service.product.impl;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.ProductRatingStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.dto.request.ProductRequestParamsDTO;
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
    public ProductRatingStatus getRatingStatus(String username, long productId) {
        Optional<Product> purchasedProductOpt =
                productRepository.findOptionalRatingProductUsername(
                        username, productId, OrderStatus.COMPLETED, ReservationStatus.COMPLETED, false);

        if (purchasedProductOpt.isEmpty()) {
            return ProductRatingStatus.NOT_PURCHASED;
        }

        Product purchasedProduct = purchasedProductOpt.get();

        boolean hasRating = purchasedProduct.getRatings().stream()
                .anyMatch(rating ->
                        rating.getCustomer().getProfile().getUser().getUsername().equals(username));

        return hasRating ? ProductRatingStatus.PURCHASED_AND_RATED : ProductRatingStatus.PURCHASED_NOT_RATED;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProduct(long productId, Boolean isDeleted) {
        Optional<Product> product;
        if(isDeleted==null){
            product = productRepository.findById(productId);
        }else{
            product = productRepository.findByProductIdAndIsDeleted(productId, isDeleted);
        }
        return product.map(this::setImageUrl)
                .map(mapper::toProductDTO)
                .orElseThrow(()-> new NotFoundException(Product.class.getSimpleName(), "id", String.valueOf(productId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntity(long productID) {
        return productRepository.findByProductIdAndIsDeletedFalse(productID)
                .orElseThrow(() -> new NotFoundException(Product.class.getSimpleName(), productID));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getRatingProductEntity(String username, long productId) {
        return productRepository.findOptionalRatingProductUsername(username, productId, OrderStatus.COMPLETED, ReservationStatus.COMPLETED, Boolean.FALSE);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(ProductRequestParamsDTO requestParamDTO) {
        Pageable pageable = PageRequest.of(
                requestParamDTO.getPage(),
                requestParamDTO.getSize(),
                Sort.by(requestParamDTO.getDirection(), requestParamDTO.getField())
        );

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasCategoryId(requestParamDTO.getCategoryId()))
                .and(ProductSpecification.hasIsDeleted(requestParamDTO.getIsDeleted()))
                .and(ProductSpecification.isFeatured(requestParamDTO.getIsFeatured()))
                .and(ProductSpecification.hasSearchValue(requestParamDTO.getSearch()));

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
