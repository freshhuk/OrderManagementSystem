package org.whiletrue.ordermanagementsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whiletrue.ordermanagementsystem.Repository.UserRepository;
import org.whiletrue.ordermanagementsystem.Services.Security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whiletrue.ordermanagementsystem.Domain.Models.Security.AuthResponse;
import org.whiletrue.ordermanagementsystem.Domain.Models.Security.LoginRequest;
import org.whiletrue.ordermanagementsystem.Domain.Models.Security.RegisterRequest;
import org.whiletrue.ordermanagementsystem.Domain.Entity.User;
import org.whiletrue.ordermanagementsystem.Domain.Enums.Role;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
