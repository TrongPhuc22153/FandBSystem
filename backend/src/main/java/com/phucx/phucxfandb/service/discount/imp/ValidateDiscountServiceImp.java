package com.phucx.phucxfandb.service.discount.imp;

import com.phucx.phucxfandb.dto.request.ValidateProductDiscountsDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.entity.Discount;
import com.phucx.phucxfandb.repository.DiscountRepository;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.discount.ValidateDiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateDiscountServiceImp implements ValidateDiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    public final static int MAX_APPLIED_DISCOUNT_PRODUCT=2;

    @Override
    public ResponseDTO<Boolean> validateDiscountsOfProduct(ValidateProductDiscountsDTO validateProductDiscountsDTO){
        log.info("validateDiscountsOfProduct(validateProductDiscountsDTO={})", validateProductDiscountsDTO);
        // Extract DTO fields
        long productId = validateProductDiscountsDTO.getProductId();
        List<String> discountIds = validateProductDiscountsDTO.getDiscountIds();
        LocalDateTime appliedDate = validateProductDiscountsDTO.getAppliedDate();

        // Check if product exists
        if (!productRepository.existsById(productId)) {
            log.warn("Product with ID {} does not exist", productId);
            return ResponseDTO.<Boolean>builder()
                    .error(String.format("Product with ID %d does not exist", productId))
                    .data(false)
                    .build();
        }

        // Find discounts that match both productId and discountIds
        List<Discount> discounts = discountRepository.findByProductsProductIdAndActiveTrueAndDiscountIdIn(productId, discountIds);
        if (discounts.size() != discountIds.size()) {
            log.warn("Some discount IDs {} not found or not associated with product ID {}. Found: {}",
                    discountIds, productId,
                    discounts.stream().map(Discount::getDiscountId).collect(Collectors.toList()));
            return ResponseDTO.<Boolean>builder()
                    .error("Some discount IDs not found or not associated with product ID")
                    .data(false)
                    .build();
        }

        // Validate appliedDate
        boolean allDiscountsActive = discounts.stream()
                .allMatch(d -> isDiscountActive(d, appliedDate));
        if (!allDiscountsActive) {
            log.warn("Some discounts are not active at appliedDate {}", appliedDate);
            return ResponseDTO.<Boolean>builder()
                    .error(String.format("Some discounts are not active at appliedDate %s", appliedDate.toString()))
                    .data(false)
                    .build();
        }

        log.info("All discounts {} are valid for product ID {} at {}", discountIds, productId, appliedDate);
        return ResponseDTO.<Boolean>builder()
                .message("All discounts are valid")
                .data(true)
                .build();
    }

    private boolean isDiscountActive(Discount discount, LocalDateTime appliedDate) {
        LocalDateTime startDate = discount.getStartDate();
        LocalDateTime endDate = discount.getEndDate();
        if (startDate == null || endDate == null) {
            return true; // No date restrictions
        }
        return !appliedDate.isBefore(startDate) && !appliedDate.isAfter(endDate);
    }
}
