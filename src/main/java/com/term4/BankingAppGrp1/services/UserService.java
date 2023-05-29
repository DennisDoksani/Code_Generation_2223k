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

        validateRegistration(registrationDTO);

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

    private void validateRegistration(RegistrationDTO registrationDTO){
        validateBsn(registrationDTO.bsn());
        
    }

    private void validateBsn(Integer bsn){
        //Convert Integer to String to check length
        String bsnString = bsn.toString();
        
        //BSN length has to be 8 or 9
        if(bsnString.length() != 9 || bsnString.length() != 8){
            throw new IllegalArgumentException("Invalid BSN");
        }
        //If length is 8, then we have to prepend a 0 to make length 9.
        else if (bsnString.length() == 8){
            bsnString = "0" + bsnString;
        }
        //Convert BSN String to int array
        int[] bsnArray = new int[9];
        for(int i = 0; i < 9; i++){
            bsnArray[i] = Character.getNumericValue(bsnString.charAt(i));
        }

        //Check valid BSN using the following formula:
        //((9 × A) + (8 × B) + (7 × C) + (6 × D) + (5 × E) + (4 × F) + (3 × G) + (2 × H) + (-1 × I)) % 11 == 0
        int sum = 0;
        for(int i = 0; i < 8; i++){
            sum += (9 - i) * bsnArray[i];
        }
        sum += (-1 * bsnArray[8]);

        if(sum % 11 != 0)
            throw new IllegalArgumentException("Invalid BSN");
    }
}
