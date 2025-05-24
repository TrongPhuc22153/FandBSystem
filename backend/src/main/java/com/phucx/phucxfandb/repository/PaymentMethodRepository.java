package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String>{

    boolean existsByMethodName(String methodName);

    Optional<PaymentMethod> findByMethodName(String name);

}
