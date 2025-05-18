package com.phucx.phucxfandb.service.image.imp;

import com.phucx.phucxfandb.constant.ApiEndpoint;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class ImageReaderServiceImpl implements ImageReaderService {
    @Value("${image.directory}")
    private String imageDirectory;
    @Value("${image.base-url}")
    private String baseUrl;


    @Override
    public InputStream getImage(String imageName) throws IOException {
        Path path = Paths.get(imageDirectory, imageName);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new NotFoundException("Image", "name", imageName);
        }
        return Files.newInputStream(path);
    }

    @Override
    public String getMimeType(String imageName) throws IOException {
        String storedLocation = imageDirectory;
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imageName);
        return Files.probeContentType(path);
    }

    @Override
    public String getImageUrl(String imageName) {
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        return normalizedBaseUrl + ApiEndpoint.IMAGES_ENDPOINT + "/" + imageName;
    }
}
