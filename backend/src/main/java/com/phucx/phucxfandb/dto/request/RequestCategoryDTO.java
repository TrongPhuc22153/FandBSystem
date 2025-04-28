package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCategoryDTO {

    private Long categoryId;

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 1, max = 40, message = "Category name must be between 1 and 40 characters")
    private String categoryName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 255, message = "Picture URL cannot exceed 255 characters")
    private String picture;

}
