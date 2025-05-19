package com.phucx.phucxfandb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import com.phucx.phucxfandb.dto.request.RequestUserDTO;
import com.phucx.phucxfandb.dto.request.UpdateUserPasswordDTO;
import com.phucx.phucxfandb.dto.request.UserRequestParamDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.service.user.UserReaderService;
import com.phucx.phucxfandb.service.user.UserUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "User endpoint")
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserReaderService userReaderService;
    private final UserUpdateService userUpdateService;


    @GetMapping("/me")
    @Operation(summary = "Get user information", description = "Authenticated access")
    public ResponseEntity<UserDTO> getUserInfo(Principal principal){
        UserDTO user = userReaderService.getUserByUsername(principal.getName());
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    @Operation(summary = "Get users", description = "Admin access")
    public ResponseEntity<Page<UserDTO>> getUsers(@ModelAttribute UserRequestParamDTO params){
        Page<UserDTO> users = userReaderService.getUsers(params);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get user information", description = "Admin access")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable String userId
    ){
        UserDTO user = userReaderService.getUserByUserId(userId);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user", description = "Admin access")
    public ResponseEntity<ResponseDTO<UserDTO>> createUser(
            @Valid @RequestBody RequestUserDTO requestUserDTO
    ){
        var userDTO = userUpdateService.createUser(requestUserDTO);
        ResponseDTO<UserDTO> responseDTO = ResponseDTO.<UserDTO>builder()
                .message("User created successfully")
                .data(userDTO)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("{userId}")
    @JsonView(Views.UpdateUserEnabledStatus.class)
    @Operation(summary = "Update user enabled status", description = "Admin access")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUserEnabledStatus(
            @PathVariable String userId,
            @Validated(ValidationGroups.UpdateUserEnabledStatus.class) @RequestBody RequestUserDTO requestUserDTO
    ){
        var data = userUpdateService.updateUserEnabledStatus(userId, requestUserDTO);
        ResponseDTO<UserDTO> responseDTO = ResponseDTO.<UserDTO>builder()
                .message("User updated successfully")
                .data(data)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change password endpoint", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody UpdateUserPasswordDTO updateUserPasswordDTO){
        userUpdateService.updateUserPassword(authentication, updateUserPasswordDTO);

        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Your password updated successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
