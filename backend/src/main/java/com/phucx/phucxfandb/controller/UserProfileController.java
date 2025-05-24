package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestUserProfileDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.service.user.UserProfileReaderService;
import com.phucx.phucxfandb.service.user.UserProfileUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User Profile API", description = "User Profile endpoint")
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProfileController {
    private final UserProfileReaderService userProfileReaderService;
    private final UserProfileUpdateService userProfileUpdateService;

    @GetMapping("{id}")
    @Operation(summary = "Get user profile", description = "Admin access")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String id){
        var profileDTO = userProfileReaderService.getUserProfileByUserId(id);
        return ResponseEntity.ok(profileDTO);
    }

    @GetMapping("/me")
    @Operation(summary = "Get authenticated user profile", description = "Authenticated access")
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal){
        var profileDTO = userProfileReaderService.getUserProfileByUsername(principal.getName());
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update authenticated user profile", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<UserProfileDTO>> updateUserProfile(
            Principal principal,
            @Valid @RequestBody RequestUserProfileDTO requestUserProfileDTO){
        var profileDTO = userProfileUpdateService.updateUserProfile(principal.getName(), requestUserProfileDTO);
        ResponseDTO<UserProfileDTO> responseDTO = ResponseDTO.<UserProfileDTO>builder()
                .message("Your profile updated successfully")
                .data(profileDTO)
                .build();
        return ResponseEntity.ok(responseDTO);
    }
}
