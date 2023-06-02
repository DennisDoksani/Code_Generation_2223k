package com.term4.BankingAppGrp1.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.services.BankingUserDetailsService;
import com.term4.BankingAppGrp1.services.UserService;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationTime;

    private final BankingUserDetailsService bankingUserDetailsService;
    private final JwtKeyProvider jwtKeyProvider;
    private final UserService userService;

    public JwtTokenProvider(BankingUserDetailsService bankingUserDetailsService, JwtKeyProvider jwtKeyProvider, UserService userService) {
        this.bankingUserDetailsService = bankingUserDetailsService;
        this.jwtKeyProvider = jwtKeyProvider;
        this.userService = userService;
    }

    public String createToken(long id, List<Role> roles) throws JwtException{
        
        //Create claims
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("auth", roles);
        
        //Create iat and exp
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationTime);

        //Create JWT token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(jwtKeyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtKeyProvider.getPrivateKey()).build().parseClaimsJws(token);
        long id = Long.parseLong(claims.getBody().getSubject());
        User user = userService.getUser(id);
        UserDetails userDetails = bankingUserDetailsService.loadUserByUsername(user.getEmail());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}