package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.repository.RoleRepository;
import com.phucx.phucxfandb.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Data
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "data")
public class DataInitializer {
    private String adminName;
    private String adminPassword;
    private String adminEmail;
    private String adminFirstName;
    private String adminLastName;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            if (!userRepository.existsByUsername(adminName)) {
                Set<Role> adminRoles = roleRepository.findByRoleNameInAndIsDeletedFalse(
                        Set.of(RoleName.ADMIN, RoleName.EMPLOYEE));

                Employee employee = Employee.builder()
                        .isDeleted(false)
                        .build();

                UserProfile profile = UserProfile.builder()
                        .isDeleted(false)
                        .build();

                User admin = User.builder()
                        .username(adminName)
                        .email(adminEmail)
                        .firstName(adminFirstName)
                        .lastName(adminLastName)
                        .enabled(true)
                        .emailVerified(true)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(adminRoles)
                        .build();

                admin.setProfile(profile);
                profile.setUser(admin);
                profile.setEmployee(employee);
                employee.setProfile(profile);

                userRepository.save(admin);
                log.info("Admin user created.");
            }
        };
    }
}
