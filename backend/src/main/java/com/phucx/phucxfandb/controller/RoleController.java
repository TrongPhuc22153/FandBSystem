package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Role API", description = "Admin operations for roles")
public class RoleController {
    private final RoleReaderService roleReaderService;

    @GetMapping
    @Operation(summary = "Get roles", description = "Admin access")
    public ResponseEntity<Set<RoleDTO>> getRoles(){
        log.info("getRoles()");
        var roles = roleReaderService.getRoles();
        return ResponseEntity.ok(roles);
    }
}
