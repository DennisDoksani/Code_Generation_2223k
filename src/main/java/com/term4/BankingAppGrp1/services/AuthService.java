package com.term4.BankingAppGrp1.services;

import javax.naming.AuthenticationException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;


@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(String email, String password) throws AuthenticationException {
        // See if a user with the provided username exists or throw exception
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Check if the password hash matches
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            // Return a JWT to the client
            return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        } 
        else {
            throw new AuthenticationException("Invalid username/password");
        }
    }


    
}
