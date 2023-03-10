package com.furniture.backend.controller;

import com.furniture.backend.exception.AppException;
import com.furniture.backend.exception.FurnitureApiException;
import com.furniture.backend.model.role.Role;
import com.furniture.backend.model.role.RoleName;
import com.furniture.backend.model.user.User;
import com.furniture.backend.payload.JwtAuthenticationResponse;
import com.furniture.backend.payload.LoginRequest;
import com.furniture.backend.payload.SignUpRequest;
import com.furniture.backend.payload.response.ApiResponse;
import com.furniture.backend.repository.RoleRepository;
import com.furniture.backend.repository.UserRepository;
import com.furniture.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String USER_ROLE_NOT_SET = "User role not set";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        String jwt;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            jwt = jwtTokenProvider.generateToken(authentication);
        }
        catch (Exception e) {
           throw new FurnitureApiException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không chính xác");
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new FurnitureApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        String firstName = signUpRequest.getFirstName().toLowerCase();

        String lastName = signUpRequest.getLastName().toLowerCase();

        String email = signUpRequest.getEmail().toLowerCase();

        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(firstName, lastName, email, password);

        List<Role> roles = new ArrayList<>();

        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        }

        user.setRoles(roles);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
    }
}
