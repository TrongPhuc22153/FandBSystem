package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.service.user.UserReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "User endpoint")
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserReaderService userReaderService;

    @GetMapping("/me")
    @Operation(summary = "Get user information", description = "Authenticated access")
    public ResponseEntity<UserDTO> getUserInfo(Principal principal){
        log.info("getUserInfo(username={})", principal.getName());
        UserDTO user = userReaderService.getUserByUsername(principal.getName());
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    @Operation(summary = "Get users", description = "Admin access")
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "role")RoleName roleName
    ){
        log.info("getUsers(roleName={})", roleName);
        Page<UserDTO> users = userReaderService.getUsersByRole(roleName, pageNumber, pageSize);
        return ResponseEntity.ok().body(users);
    }


//    @Operation(summary = "Update user password", description = "User access")
//    @PatchMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseDTO<Boolean>> changePassword(
//            @Valid @RequestBody UserChangePasswordDTO userChangePassword
//    ) throws UserPasswordException {
//        boolean result = userPasswordService.changePassword(userChangePassword);
//        ResponseDTO<Boolean> responseDTO = ResponseDTO.<Boolean>builder()
//                .message("Password updated successfully")
//                .data(result)
//                .build();
//        return ResponseEntity.ok().body(responseDTO);
//    }
}
