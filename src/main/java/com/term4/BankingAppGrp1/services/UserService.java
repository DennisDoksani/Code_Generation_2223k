package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import javax.naming.AuthenticationException;

@Service
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(User id) {
        //supposedly the exist and deleteById are methods provided by the Spring Data JPA framework right? Why this error then?
        //Solved problem by adding id.getId(). Correct?

        if (userRepository.existsById(id.getId())){
            userRepository.deleteById(id.getId());
            return "User deleted successfully";
        } else {
            return "User not found in the database";
        }
    }

    @Override
    public Optional<User> getUser(User id) {
        return userRepository.findById(id.getId());
    }

    public String login(String email, String password) throws Exception {
        // See if a user with the provided username exists or throw exception
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Check if the password hash matches
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            // Return a JWT to the client
            return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        } else {
            throw new AuthenticationException("Invalid username/password");
        }
    }

}
