package com.phucx.phucxfandb.service.image.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class CloudinaryImageUpdateService implements ImageUpdateService {

    private final Cloudinary cloudinary;

    @Override
    public ImageDTO uploadImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", generatePublicId(file.getOriginalFilename()),
                            "resource_type", "image"
                    ));
            return ImageDTO.builder()
                    .imageUrl((String) uploadResult.get("secure_url"))
                    .build();
        } catch (IOException e) {
            throw new Exception("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ImageDTO> uploadImages(MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Files array cannot be null or empty");
        }

        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return uploadImage(file);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeImages(List<String> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Images list cannot be null or empty");
        }

        try {
            for (String image : images) {
                cloudinary.uploader().destroy(image, ObjectUtils.asMap("resource_type", "image"));
            }
        }catch (IllegalArgumentException e) {
            log.warn("Skipping invalid image filename: {}", e.getMessage());
        } catch (IOException ex) {
            log.error("Error: {}", ex.getMessage());
        }
    }

    private String generatePublicId(String originalFilename) {
        String name = originalFilename != null ? originalFilename.split("\\.")[0] : "image";
        return name + "_" + System.currentTimeMillis();
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String path = uri.getPath();
            // Extract the public_id (part after /upload/ and before the extension)
            String[] parts = path.split("/upload/")[1].split("\\.");
            return parts[0].replaceAll("^v\\d+/", ""); // Remove version prefix (e.g., v1234567890)
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Cloudinary URL: " + imageUrl, e);
        }
    }
}
