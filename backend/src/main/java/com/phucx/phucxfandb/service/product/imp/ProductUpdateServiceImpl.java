package com.phucx.phucxfandb.service.product.imp;

import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ProductMapper;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUpdateServiceImpl implements ProductUpdateService {
    private final ProductRepository productRepository;
    private final CategoryReaderService categoryReaderService;
    private final ProductMapper mapper;

    @Override
    @Modifying
    @Transactional
    public Product updateProductInStock(long productId, int quantity) {
        log.info("updateProductInStock(productId={}, quantity={})", productId, quantity);
        // Find existing product
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product", "id", productId));
        product.setUnitsInStock(quantity);
        return productRepository.save(product);
    }

    @Override
    @Modifying
    @Transactional
    public ProductDTO updateProduct(long productId, RequestProductDTO requestProductDTO) {
        log.info("updateProduct(productId={}, requestProductDTO={})", productId, requestProductDTO);
        // Find existing product
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product", "id", productId));
        Category category = categoryReaderService.getCategoryEntity(requestProductDTO.getCategoryId());
        // Update product fields
        mapper.updateProductFromDTO(requestProductDTO, category, product);
        // Save updated product
        Product updatedProduct = productRepository.save(product);
        // Map to DTO and return
        return mapper.toProductDTO(updatedProduct);
    }

    @Override
    @Modifying
    @Transactional
    public ProductDTO createProduct(RequestProductDTO requestProductDTO) {
        log.info("createProduct(requestProductDTO={})", requestProductDTO);
        // Find existing product
        if(productRepository.existsByProductName(requestProductDTO.getProductName())) {
            throw new EntityExistsException(String.format("Product with name %s already exists", requestProductDTO.getProductName()));
        }
        Category category = categoryReaderService.getCategoryEntity(requestProductDTO.getCategoryId());
        // Map DTO to entity
        Product product = mapper.toProduct(requestProductDTO, category);
        // Save new product
        Product savedProduct = productRepository.save(product);
        // Map to DTO and return
        return mapper.toProductDTO(savedProduct);
    }

    @Override
    @Modifying
    @Transactional
    public List<ProductDTO> createProducts(List<RequestProductDTO> requestProductDTOs) {
        log.info("createProducts(requestProductDTOs={})", requestProductDTOs);

        List<Product> productsToSave = requestProductDTOs.stream()
                .map(dto -> {
                    Category category = categoryReaderService.getCategoryEntity(dto.getCategoryId());
                    return mapper.toProduct(dto, category);
                })
                .collect(Collectors.toList());

        return  productRepository.saveAll(productsToSave).stream()
                .map(mapper::toProductDTO)
                .collect(Collectors.toList());
    }

}
