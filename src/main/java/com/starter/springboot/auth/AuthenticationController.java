package com.starter.springboot.auth;


import com.starter.springboot.rest.dto.LoginDTO;
import com.starter.springboot.rest.dto.VerifyTokenRequestDTO;
import com.starter.springboot.security.jwt.JWTToken;
import com.starter.springboot.security.jwt.TokenProvider;
import com.starter.springboot.services.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping(value = "/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDTO loginDTO)
    {
        log.debug("Credentials: {}", loginDTO);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(), loginDTO.getPassword()
        );
        try
        {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            String token = tokenProvider.createToken(authentication, loginDTO.isRememberMe());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(new JWTToken(token), HttpStatus.OK);
        }
        catch (AuthenticationException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "verify")
    public ResponseEntity<JWTToken> verifyOtp(@Valid @RequestBody VerifyTokenRequestDTO verifyTokenRequest)
    {
        String username = verifyTokenRequest.getUsername();
        Integer otp = verifyTokenRequest.getOtp();
        Boolean rememberMe = verifyTokenRequest.getRememberMe();

        boolean isOtpValid = otpService.validateOTP(username, otp);
        if (!isOtpValid) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = tokenProvider.createTokenAfterVerifiedOtp(username, rememberMe);
        JWTToken response = new JWTToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
