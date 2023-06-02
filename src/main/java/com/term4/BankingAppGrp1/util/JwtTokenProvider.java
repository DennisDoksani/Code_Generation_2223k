package com.term4.BankingAppGrp1.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.services.BankingUserDetailsService;
import java.util.Date;
import java.util.List; 

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationTime;

    private final BankingUserDetailsService bankingUserDetailsService;
    private final JwtKeyProvider jwtKeyProvider;

    public JwtTokenProvider(BankingUserDetailsService bankingUserDetailsService, JwtKeyProvider jwtKeyProvider) {
        this.bankingUserDetailsService = bankingUserDetailsService;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String createToken(String email, long id, List<Role> roles) throws JwtException{
        
        //Create claims
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("auth", roles);
        claims.put("id", id);
        
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
        String email = claims.getBody().getSubject();
        UserDetails userDetails = bankingUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}