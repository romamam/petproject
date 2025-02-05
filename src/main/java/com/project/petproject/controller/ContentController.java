package com.project.petproject.controller;

import com.mysql.cj.protocol.AuthenticationProvider;
import com.project.petproject.CustomUserDetailService;
import com.project.petproject.webtoken.JwtService;
import com.project.petproject.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
public class ContentController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @GetMapping("/home")
    public String handleWelcome(){
        return "home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome(){
        return "home_admin";
    }

    @GetMapping("/user/home")
    public String handleUserHome(){
        return "home_user";
    }

    @GetMapping("/login")
    public String handleLogin(){
        return "custom_login";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticateAntGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password())
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(customUserDetailService.loadUserByUsername(loginForm.username()));
            return ResponseEntity.ok(Map.of("token", token)); // JSON-відповідь
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
