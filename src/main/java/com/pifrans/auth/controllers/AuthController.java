package com.pifrans.auth.controllers;

import com.pifrans.auth.constants.HeadersKeys;
import com.pifrans.auth.constants.HeadersValues;
import com.pifrans.auth.securities.TokenJWTSecurity;
import com.pifrans.auth.securities.UserDetailsSecurity;
import com.pifrans.auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenJWTSecurity tokenJWTSecurity;

    public AuthController(TokenJWTSecurity tokenJWTSecurity) {
        this.tokenJWTSecurity = tokenJWTSecurity;
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletResponse response){
        try {
            UserDetailsSecurity userDetailsSecurity = UserService.userLogged();
            String token = tokenJWTSecurity.generateToken(userDetailsSecurity != null ? userDetailsSecurity.getUsername() : null);
            response.addHeader(HeadersKeys.X_AUTHORIZATION.getDescription(), HeadersValues.BEARER.getDescription() + token);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
