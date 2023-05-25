package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;
import com.term4.BankingAppGrp1.models.Role;

import jakarta.persistence.EntityNotFoundException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final SimpleDateFormat dateFormatYMD;

    public UserService(UserRepository userRepository, SimpleDateFormat dateFormatYMD) {
        this.userRepository = userRepository;
        this.dateFormatYMD = dateFormatYMD;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User registerUser(RegistrationDTO registrationDTO) {
        User newUser = new User(registrationDTO.bsn(), 
                                registrationDTO.firstName(), 
                                registrationDTO.lastName(),
                                LocalDate.parse(registrationDTO.dateOfBirth()),
                                registrationDTO.phoneNumber(), 
                                registrationDTO.email(), 
                                registrationDTO.password());
                                
        return userRepository.save(newUser);
    }

    public String deleteUser(long id) {

        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
            return "User deleted successfully";
        } 
        else {
            return "User not found in the database";
        }
    }
    public User getUser(long id)  throws EntityNotFoundException{ 
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
    }

}
