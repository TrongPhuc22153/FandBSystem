package com.phucx.phucxfandb.service.product.impl;

import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ProductMapper;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import com.phucx.phucxfandb.utils.ImageUtils;
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
    private final ImageUpdateService imageUpdateService;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public Product updateProductInStock(long productId, int quantity) {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException(Product.class.getSimpleName(), "id", productId));
        product.setUnitsInStock(quantity);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProductQuantity(long productId, int quantity) {
        Product updated = this.updateProductInStock(productId, quantity);
        return mapper.toProductDTO(updated);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(long productId, RequestProductDTO requestProductDTO) {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException(Product.class.getSimpleName(), "id", productId));

        if(requestProductDTO.getPicture()!=null && !requestProductDTO.getPicture().isEmpty()){
            String newImageName = ImageUtils.extractImageNameFromUrl(requestProductDTO.getPicture());
            if(product.getPicture() != null && !product.getPicture().isBlank() ){
                if(!newImageName.equalsIgnoreCase(product.getPicture())){
                    imageUpdateService.removeImages(List.of(product.getPicture()));
                    requestProductDTO.setPicture(newImageName);
                }else{
                    requestProductDTO.setPicture(product.getPicture());
                }
            }else{
                requestProductDTO.setPicture(newImageName);
            }
        }

        Category category = categoryReaderService.getCategoryEntity(requestProductDTO.getCategoryId());
        mapper.updateProductFromDTO(requestProductDTO, category, product);
        Product updatedProduct = productRepository.save(product);
        return mapper.toProductDTO(updatedProduct);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(RequestProductDTO requestProductDTO) {
        if(productRepository.existsByProductName(requestProductDTO.getProductName())) {
            throw new EntityExistsException(String.format("Product with name %s already exists", requestProductDTO.getProductName()));
        }
        if(requestProductDTO.getPicture()!=null && !requestProductDTO.getPicture().isEmpty()){
            String imageName = ImageUtils.extractImageNameFromUrl(requestProductDTO.getPicture());
            requestProductDTO.setPicture(imageName);
        }

        Category category = categoryReaderService.getCategoryEntity(requestProductDTO.getCategoryId());
        Product product = mapper.toProduct(requestProductDTO, category);
        Product savedProduct = productRepository.save(product);
        return mapper.toProductDTO(savedProduct);
    }

    @Override
    @Modifying
    @Transactional
    public ProductDTO updateProductIsDeletedStatus(long id, RequestProductDTO requestProductDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Product.class.getSimpleName(), id));
        existingProduct.setIsDeleted(requestProductDTO.getIsDeleted());
        Product updated = productRepository.save(existingProduct);
        return mapper.toProductDTO(updated);
    }

    @Override
    @Transactional
    public List<ProductDTO> createProducts(List<RequestProductDTO> requestProductDTOs) {
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
