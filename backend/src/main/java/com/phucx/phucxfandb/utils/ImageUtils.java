package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ImageUtils {
    
    public static String uploadImage(MultipartFile file, String imageLocation) throws IOException, NotFoundException {
        log.info("uploadImage(file={}, imageLocation={})", file.getOriginalFilename(), imageLocation);
        if(file.isEmpty()) throw new NotFoundException("Your Image does not found");
        // set filename
        String filename = file.getOriginalFilename();
        if(filename!=null){
            int dotIndex = filename.lastIndexOf(".");
            String extension = filename.substring(dotIndex+1);
            String randomName = UUID.randomUUID().toString();
            String newFile = randomName + "." + extension;
            // set stored location
            Path targetPath = Path.of(imageLocation, newFile);
            // copy image to stored location
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return newFile;
        }
        return null;
    }

    public static String getCurrentUrl(String requestUri, Integer serverPort, String imageUri) {
        String host = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return host + ":" + serverPort + imageUri;
    }

    public static String getUri(String requestUri){
        int slashIndex = requestUri.lastIndexOf("/");
        return requestUri.substring(0, slashIndex);
    }

    public static String getMimeType(String imageName, String imageLocation) throws IOException {
        String storedLocation = imageLocation;
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imageName);
        return Files.probeContentType(path);
    }

    public static String getImageName(String url) {
        if(url==null || url.isEmpty()) return null;
        int indexOfSlash = url.lastIndexOf("/");
        return url.substring(indexOfSlash + 1);
    }

    public static byte[] getImage(String imageName, String imageLocation) throws IOException {
        String filePath = imageLocation;
        if(filePath.charAt(filePath.length()-1)!='/') filePath+='/';
        Path path = Paths.get(filePath+imageName);
        return Files.readAllBytes(path);
    }

    public static UserProfile setUserProfileImage(UserProfile userProfile, String serverName, String imageUri) {
        // filtering customer 
        if(!(userProfile.getPicture()!=null && !userProfile.getPicture().isEmpty())) return userProfile;
        // userProfile has image
        String imageUrl = "/" + serverName + imageUri;
        userProfile.setPicture(imageUrl + "/" + userProfile.getPicture());
        return userProfile;

    }

    public static List<UserProfile> setUserProfileImage(List<UserProfile> userProfiles, String serverName, String imageUri) {
        userProfiles.forEach(user ->{
            if(user.getPicture()!=null && !user.getPicture().isEmpty()){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                user.setPicture(uri + "/" + user.getPicture());
            }
        });
        return userProfiles;
    }
}
