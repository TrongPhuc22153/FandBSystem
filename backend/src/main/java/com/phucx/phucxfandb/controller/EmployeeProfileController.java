package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestEmployeeDTO;
import com.phucx.phucxfandb.dto.response.EmployeeDTO;
import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.employee.EmployeeUpdateService;
import com.phucx.phucxfandb.service.image.EmployeeImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Employee Profile API", description = "Employee endpoint")
@RequestMapping(value = "/api/v1/employees/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeProfileController {
    private final EmployeeReaderService employeeReaderService;
    private final EmployeeUpdateService employeeUpdateService;
    private final EmployeeImageService employeeImageService;

    @Operation(summary = "Get employee information", description = "Employee access")
    @GetMapping("/me")
    public ResponseEntity<EmployeeDTO> getEmployeeInfo(Principal principal){
        EmployeeDTO employee = employeeReaderService.getEmployeeDetailsByUsername(principal.getName());
        return ResponseEntity.ok().body(employee);
    }

    @Operation(summary = "Update employee information", description = "Employee access")
    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<EmployeeDTO>> updateEmployeeInfo(
            Principal principal,
            @Valid @RequestBody RequestEmployeeDTO requestEmployeeDTO
    ){
        EmployeeDTO updatedEmployee = employeeUpdateService.updateEmployeeByUsername(principal.getName(), requestEmployeeDTO);
        ResponseDTO<EmployeeDTO> responseDTO = ResponseDTO.<EmployeeDTO>builder()
                .message("Your profile updated successfully")
                .data(updatedEmployee)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Upload employee's image", description = "Employee access")
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ImageDTO>> uploadEmployeeImage(
            @RequestPart MultipartFile file, HttpServletRequest request
    ) throws IOException {
        String filename = employeeImageService.uploadEmployeeImage(file);
        String imageUrl = employeeImageService.getCurrentUrl(request) + "/" + filename;
        ImageDTO imageDTO = ImageDTO.builder()
                .imageUrl(imageUrl)
                .build();
        ResponseDTO<ImageDTO> responseDTO = ResponseDTO.<ImageDTO>builder()
                .message("Your image uploaded successfully")
                .data(imageDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Get employee image", description = "Public access")
    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) throws IOException {
        byte[] image = employeeImageService.getEmployeeImage(imageName);
        String mimeType = employeeImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
}
