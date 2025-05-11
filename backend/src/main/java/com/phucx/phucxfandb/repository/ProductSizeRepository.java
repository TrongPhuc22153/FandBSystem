package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, String>{
    Optional<ProductSize> findByProductProductId(long productId);

}
