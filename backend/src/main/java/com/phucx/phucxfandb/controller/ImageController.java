package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/images")
@Tag(name = "Image API", description = "Image operation")
public class ImageController {
    private final ImageReaderService imageReaderService;
    private final ImageUpdateService imageUpdateService;

    @GetMapping("/{imageName}")
    @Operation(summary = "Get image", description = "Public access")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageName) throws IOException {
        InputStream image = imageReaderService.getImage(imageName);
        String mimeType = imageReaderService.getMimeType(imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(image));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image",  description = "Authenticated access")
    public ResponseEntity<ResponseDTO<List<ImageDTO>>> uploadProductImage(
            @Valid @NotNull @RequestPart("files") MultipartFile[] files
    ) throws Exception {
        List<ImageDTO> images = imageUpdateService.uploadImages(files);
        ResponseDTO<List<ImageDTO>> responseDTO = ResponseDTO.<List<ImageDTO>>builder()
                .message("Your images uploaded successfully")
                .data(images)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
