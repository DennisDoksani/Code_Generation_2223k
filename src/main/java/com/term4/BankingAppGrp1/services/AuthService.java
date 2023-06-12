package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.responseDTOs.LoginResponseDTO;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;
import javax.naming.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
      JwtTokenProvider jwtTokenProvider) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public LoginResponseDTO login(String email, String password) throws AuthenticationException {
    // See if a user with the provided username exists or throw exception (deliberately vague)
    User user = userRepository.findByEmailEqualsIgnoreCase(email)
        .orElseThrow(() -> new AuthenticationException("Invalid username/password"));

    // Check if the password hash matches
    if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
      // Create token if credentials are correct and return LoginResponseDTO
      String jwt = jwtTokenProvider.createToken(user.getId(), user.getRoles());
      return new LoginResponseDTO(jwt, user.getId(), user.getEmail(), user.getFullName());
    } else {
      //Otherwise throw exception
      throw new AuthenticationException("Invalid username/password");
    }
  }
}
