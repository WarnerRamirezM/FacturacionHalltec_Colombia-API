package com.hernan.oauth_client.Controllers;

import com.hernan.oauth_client.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/get-token")
    public String getToken() {
        return authService.getAccessToken();
    }
}
