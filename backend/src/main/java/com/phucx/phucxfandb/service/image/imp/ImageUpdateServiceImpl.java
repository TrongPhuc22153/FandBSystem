package com.phucx.phucxfandb.service.image.imp;

import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import com.phucx.phucxfandb.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class ImageUpdateServiceImpl implements ImageUpdateService {
    @Value("${image.directory}")
    private String imageDirectory;
    @Value("${image.base-url}")
    private String baseUrl;

    @Override
    public ImageDTO uploadImage(MultipartFile file)  throws Exception {
        log.info("Uploading file: name={}, size={}", file.getName(), file.getSize());

        // Get and validate filename
        String filename = file.getOriginalFilename();
        String extension = getExtension(filename);

        // Generate new filename
        String newFile = UUID.randomUUID() + "." + extension;

        // Ensure directory exists
        // Create directory if it doesn't exist
        Path targetPath = Path.of(imageDirectory, newFile);
        Files.createDirectories(targetPath.getParent());

        // Copy file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        String imageUrl = ImageUtils.getCurrentUrl(baseUrl) + newFile;
        return ImageDTO.builder()
                .imageUrl(imageUrl)
                .build();
    }

    @Override
    public void removeImages(List<String> images) {
        log.info("removeImages(images={})", images);
        for (String image: images){
            File file = new File(imageDirectory + image);
            if(file.exists() && file.isFile()){
                file.delete();
            }
        }
    }

    @Override
    public List<ImageDTO> uploadImages(MultipartFile[] files) throws Exception{
        List<ImageDTO> imageDTOs = new ArrayList<>();
        for (MultipartFile file : files){
            imageDTOs.add(uploadImage(file));
        }
        return imageDTOs;
    }

    private String getExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Invalid or missing filename");
        }

        // Extract and validate extension
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("File has no extension");
        }
        String extension = filename.substring(dotIndex + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
        if (!allowedExtensions.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }
        return extension;
    }
}
