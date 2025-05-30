package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String>{

    boolean existsByMethodName(String methodName);

    Optional<PaymentMethod> findByMethodName(String name);

    List<PaymentMethod> findByTypesName(String type);

}
