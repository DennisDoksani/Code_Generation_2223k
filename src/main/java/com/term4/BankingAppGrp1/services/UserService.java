package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import javax.naming.AuthenticationException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){

        return userRepository.save(user);
    }

    public String deleteUser(long id) {

        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found in the database";
        }
    }
    public User getUser(long id)  throws AuthenticationException{ 

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        return user;
    }

    

}
