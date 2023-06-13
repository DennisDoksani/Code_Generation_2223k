package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankingUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  public BankingUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    com.term4.BankingAppGrp1.models.User userEntity =
        userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(userEntity.getEmail(),
        userEntity.getPassword(), userEntity.getRoles());
  }
}
