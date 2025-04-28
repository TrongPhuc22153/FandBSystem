package com.phucx.phucxfandb.service.product.imp;

import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ProductMapper;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductReaderServiceImpl implements ProductReaderService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProduct(long productId) {
        log.info("getProduct(productId={})", productId);
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));
        return mapper.toProductDTO(product);
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
    public Page<ProductDTO> getProducts(String field, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("getProduct(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByIsDeletedFalse(pageable);
        return products.map(mapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByName(String productName, String field, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("getProductsByName(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByIsDeletedFalse(pageable);
        return products.map(mapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsBySearch(String searchValue, String field, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("getProductsBySearch(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByProductNameContainingIgnoreCaseAndIsDeletedFalse(searchValue, pageable);
        return products.map(mapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByCategoryId(long categoryId, String field, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("getProductsByCategoryId(categoryId={}, pageNumber={}, pageSize={})", categoryId, pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByCategoryCategoryIdAndIsDeletedFalse(categoryId, pageable);
        return products.map(mapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByCategory(String categoryName, String field, Sort.Direction direction,  int pageNumber, int pageSize) {
        log.info("getProductsByCategory(categoryName={}, pageNumber={}, pageSize={})", categoryName, pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByCategoryCategoryNameAndIsDeletedFalse(categoryName, pageable);
        return products.map(mapper::toProductDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getFeaturedProducts(String field, Sort.Direction direction, int pageNumber, int pageSize) {
        log.info("getFeaturedProducts(field={}, direction={}, pageNumber={}, pageSize={})", field, direction, pageNumber, pageSize);
        Sort sort = Sort.by(direction, field);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByIsFeaturedTrueAndIsDeletedFalse(pageable);
        return products.map(mapper::toProductDTO);
    }
}
