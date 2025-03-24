package com.phucx.phucxfoodshop.service.discount;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Discount;
import com.phucx.phucxfoodshop.model.DiscountDetail;
import com.phucx.phucxfoodshop.model.DiscountType;
import com.phucx.phucxfoodshop.model.DiscountWithProduct;

public interface DiscountService {
    // insert discount
    public Discount insertDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;
    // update discount
    public Boolean updateDiscount(DiscountWithProduct discount) throws InvalidDiscountException, NotFoundException;
    public Boolean updateDiscountStatus(DiscountDetail discount) throws NotFoundException;

    // get discount
    public Page<Discount> getDiscounts(int pageNumber, int pageSize);
    public Discount getDiscount(String discountID) throws NotFoundException;
    public DiscountDetail getDiscountDetail(String discountID) throws NotFoundException;
    
    public List<DiscountDetail> getDiscountDetails(List<String> discountIDs);
    // discount type
    public Page<DiscountType> getDiscountTypes(int pageNumber, int pageSize);
    public Page<DiscountDetail> getDiscountsByProduct(int productID, int pageNumber, int pageSize) throws NotFoundException;
    public DiscountType getDiscountType(int discountTypeID) throws NotFoundException;
} 
