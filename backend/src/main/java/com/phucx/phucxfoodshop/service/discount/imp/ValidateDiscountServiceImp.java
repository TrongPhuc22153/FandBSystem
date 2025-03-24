package com.phucx.phucxfoodshop.service.discount.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.DiscountTypeConst;
import com.phucx.phucxfoodshop.exceptions.ExceedMaxDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.DiscountDetail;
import com.phucx.phucxfoodshop.model.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.repository.DiscountDetailRepository;
import com.phucx.phucxfoodshop.service.discount.ValidateDiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidateDiscountServiceImp implements ValidateDiscountService{
    public final static int MAX_APPLIED_DISCOUNT_PRODUCT=2;
    @Autowired
    private DiscountDetailRepository discountDetailRepository;

    private Boolean validateDiscountsOfProduct(ProductDiscountsDTO productDiscounts) throws NotFoundException, ExceedMaxDiscountException {
        log.info("validateDiscountsOfProduct({})", productDiscounts);
        Integer productID = productDiscounts.getProductID();
        List<DiscountDetail> discounts = discountDetailRepository.findAllByDiscountIDAndProductID(
            productDiscounts.getDiscountIDs(), productID);
        if(discounts==null || discounts.isEmpty()){
            throw new NotFoundException("Discounts do not found!");
        }
        // check product's discounts
        this.checkProductDiscounts(productID, discounts);
        // validate each discount
        for (DiscountDetail discount : discounts) {
            this.findDiscountID(productDiscounts.getDiscountIDs(), discount.getDiscountID())
                .orElseThrow(()-> new NotFoundException("Discount " + discount.getDiscountID() + " does not found!"));
            // validate discount based on type
            if(discount.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Percentage_based.getValue())){
                return this.validatePercenageBasedDiscount(discount, productDiscounts.getAppliedDate());
            }else if(discount.getDiscountType().equalsIgnoreCase(DiscountTypeConst.Code.getValue())){
                return this.validateCodeDiscount(discount, productDiscounts.getAppliedDate());
            }else {
                throw new NotFoundException("Discount type " + discount.getDiscountType() + " does not found");
            }
        }
        return false;
    }

    private void checkProductDiscounts(Integer productID, List<DiscountDetail> discounts) throws ExceedMaxDiscountException{
        if(discounts.size()>MAX_APPLIED_DISCOUNT_PRODUCT){
            throw new ExceedMaxDiscountException("Product " + productID + "exceeds maximum number of discounts!");
        }
        Map<String, Long> groupDiscounts = discounts.stream()
            .collect(Collectors.groupingBy(DiscountDetail::getDiscountType, Collectors.counting()));
        groupDiscounts.entrySet().stream().filter(entry -> entry.getValue()>1)
            .forEach(entry -> new ExceedMaxDiscountException("Exceed number of discount type " + entry.getKey()));
    }

    private Optional<String> findDiscountID(List<String> discountIDs, String discountID){
        return discountIDs.stream().filter(id->id.equals(discountID)).findFirst();
    }

    @Override
    public ResponseFormat validateDiscountsOfProducts(List<ProductDiscountsDTO> productsDiscounts) {
        log.info("validateDiscountsOfProducts({})", productsDiscounts);
        ResponseFormat responseFormat = new ResponseFormat();
        try {
            for(ProductDiscountsDTO product: productsDiscounts){
                if(product.getDiscountIDs()==null|| product.getDiscountIDs().isEmpty()) continue;
                Boolean isValid = this.validateDiscountsOfProduct(product);
                if(!isValid) throw new InvalidDiscountException("Discounts of product " + product.getProductID() + " are not valid!");
            };
            responseFormat.setStatus(true);
        } catch (NotFoundException | ExceedMaxDiscountException | InvalidDiscountException e) {
            responseFormat.setStatus(false);
            responseFormat.setError(e.getMessage());
        }
        return responseFormat;
    }

    // validate discount for code discount
    private boolean validateCodeDiscount(DiscountDetail discount, LocalDateTime appliedDate){
        log.info("validateCodeDiscount(discount={}, appliedDate={})", discount, appliedDate);
        return false;
    }

    // validate discount for percenage-based discount
    private Boolean validatePercenageBasedDiscount(DiscountDetail discount, LocalDateTime appliedDate){
        log.info("validatePercenageBasedDiscount(discount={}, appliedDate={})", discount, appliedDate);
        return this.validateDiscount(discount, appliedDate);
    }

    // validate a discount
    private Boolean validateDiscount(DiscountDetail discount, LocalDateTime appliedDate){
        log.info("validateDiscount(discount={}, appliedDate={})", discount, appliedDate);
        boolean isValid = false;
        Boolean isActive = discount.getActive();

        if((appliedDate.isEqual(discount.getStartDate()) || appliedDate.isAfter(discount.getStartDate()))&&
            (appliedDate.isEqual(discount.getEndDate()) || appliedDate.isBefore(discount.getEndDate()))){
                isValid = true;
        }
        if(!isValid)
            log.info("Discount {} is out of date", discount.getDiscountID());
        if(!isActive)
            log.info("Discount {} is not available", discount.getDiscountID());
        return isActive && isValid;
    }
}
