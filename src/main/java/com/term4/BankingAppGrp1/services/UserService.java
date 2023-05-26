package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User registerUser(RegistrationDTO registrationDTO) {
        User newUser = new User(registrationDTO.bsn(), 
                                registrationDTO.firstName(), 
                                registrationDTO.lastName(),
                                LocalDate.parse(registrationDTO.dateOfBirth()),
                                registrationDTO.phoneNumber(), 
                                registrationDTO.email(), 
                                bCryptPasswordEncoder.encode(registrationDTO.password()));
                                
        return userRepository.save(newUser);
    }

    public String deleteUser(long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } 
        else {
            return "User not found in the database";
        }
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id: " + id + " was not found"));
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
