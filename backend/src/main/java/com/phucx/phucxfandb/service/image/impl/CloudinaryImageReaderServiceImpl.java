package com.phucx.phucxfandb.service.image.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class CloudinaryImageReaderServiceImpl implements ImageReaderService {
    private final Cloudinary cloudinary;

    @Override
    public String getMimeType(String imageName) throws IOException {
        if (imageName == null || imageName.isEmpty()) {
            throw new IllegalArgumentException("Image name cannot be null or empty");
        }

        try {
            Map resource = cloudinary.api().resource(imageName, ObjectUtils.asMap("resource_type", "image"));
            String format = (String) resource.get("format");
            log.info("Retrieved MIME type for image {}: image/{}", imageName, format);
            return "image/" + format; // e.g., image/jpeg, image/png
        } catch (Exception e) {
            log.error("Failed to retrieve MIME type for image {}: {}", imageName, e.getMessage());
            throw new IOException("Failed to retrieve MIME type for image: " + imageName, e);
        }
    }

    @Override
    public String getImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            throw new IllegalArgumentException("Image name cannot be null or empty");
        }

        try {
            String url = cloudinary.url()
                    .resourceType("image")
                    .publicId(imageName)
                    .secure(true) // Use HTTPS
                    .generate();
            log.info("Generated URL for image {}: {}", imageName, url);
            return url;
        } catch (Exception e) {
            log.error("Failed to generate URL for image {}: {}", imageName, e.getMessage());
            throw new RuntimeException("Failed to generate URL for image: " + imageName, e);
        }
    }

    @Override
    public InputStream getImage(String imageName) throws IOException {
        if (imageName == null || imageName.isEmpty()) {
            throw new IllegalArgumentException("Image name cannot be null or empty");
        }

        try {
            String url = getImageUrl(imageName);
            InputStream inputStream = new URL(url).openStream();
            log.info("Retrieved InputStream for image {}", imageName);
            return inputStream;
        } catch (IOException e) {
            log.error("Failed to retrieve image {}: {}", imageName, e.getMessage());
            throw new IOException("Failed to retrieve image: " + imageName, e);
        }
    }
}
