package com.starter.springboot.security.jwt;

import com.starter.springboot.domain.User;
import com.starter.springboot.repositories.UserRepository;
import com.starter.springboot.services.OtpService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long tokenValidityInSeconds;

    @Value("${jwt.expiration}")
    private long tokenValidityInSecondsForRememberMe;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;


    /**
     * Create token from authentication
     *
     * @param authentication authentication object
     * @param rememberMe remember me indicator
     * @return String as token
     */
    public String createToken(Authentication authentication, Boolean rememberMe)
    {
        String username = authentication.getName();
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found!"));

        if (user.getIsOtpRequired())
        {
            otpService.generateOtp(user.getUsername());
            return null;
        }
        return generateToken(authentication, rememberMe);
    }

    /**
     * Create token after verified OTP code
     *
     * @param username provided username
     * @param rememberMe remember me indicator
     * @return String token value
     */
    public String createTokenAfterVerifiedOtp(String username, Boolean rememberMe)
    {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        List<GrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(), user.getPassword(), authorities
        );

        return generateToken(authentication, rememberMe);
    }

    /**
     * Method for getting authentication context.
     *
     * @param token provided token
     * @return Authentication Object
     */
    public Authentication getAuthentication(String token)
    {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        String principal = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Method for validate token.
     *
     * @param authToken - JWT token
     * @return true | false
     */
    public boolean validateToken(String authToken)
    {
        try
        {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException e)
        {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Generating token from authentication object
     *
     * @param authentication provided authentication
     * @param rememberMe remember me indicator
     * @return String value of jwt token
     */
    private String generateToken(Authentication authentication, Boolean rememberMe)
    {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity;
        if (Boolean.TRUE.equals(rememberMe)) {
            validity = new Date(now + this.tokenValidityInSecondsForRememberMe * 1000);
        }
        else {
            validity = new Date(now + this.tokenValidityInSeconds * 1000);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }
}
