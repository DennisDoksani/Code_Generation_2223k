package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

import jakarta.persistence.EntityNotFoundException;

import java.awt.print.Pageable;
import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
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

        if(!registrationDTOIsValid(registrationDTO)){
            return null;
        }

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

    //dont use at all the Role userRole
    public List<User> getAllUsers(Pageable pageable, Role usersRole) {
        Page<User> users;
       // if (usersRole != null)
         //   users = userRepository.findUserByRolesEqualsAndIdNot(pageable, usersRole, //DEFAULT_ID)
      //  else
         //   users = userRepository.findByIdNot(pageable, //DEFAULT_ID);
      //  return users.getContent();
        return null;
    }
    //dont use at all the Role userRole


    public User updateUser(UserUpdateDTO userUpdateDTO){
        User updatingUser = userRepository.findById(userUpdateDTO.bsn()).orElseThrow(() ->
                new EntityNotFoundException("The updating user with BSN: " + userUpdateDTO.bsn() + " was not found"));

        updatingUser.setFirstName(userUpdateDTO.firstName());
        updatingUser.setLastName(userUpdateDTO.lastName());
        updatingUser.setDateOfBirth(userUpdateDTO.dateOfBirth());
        updatingUser.setPhoneNumber(userUpdateDTO.phoneNumber());
        updatingUser.setEmail(userUpdateDTO.email());
        updatingUser.setPassword(bCryptPasswordEncoder.encode(userUpdateDTO.password()));
        updatingUser.setActive(userUpdateDTO.isActive());
        updatingUser.setDayLimit(userUpdateDTO.dayLimit());
        updatingUser.setTransactionLimit(userUpdateDTO.transactionLimit());

        return userRepository.save(updatingUser);
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id: " + id + " was not found"));
    }

    private boolean registrationDTOIsValid(RegistrationDTO registrationDTO){
        return true;
    }
}
