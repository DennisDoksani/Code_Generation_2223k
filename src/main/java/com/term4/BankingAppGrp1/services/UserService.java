package com.term4.BankingAppGrp1.services;

import org.springframework.stereotype.Service;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;


@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
