package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;

import jakarta.persistence.EntityNotFoundException;

import java.awt.print.Pageable;
import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PhoneNumberUtil phoneNumberUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AccountRepository accountRepository, PhoneNumberUtil phoneNumberUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountRepository = accountRepository;
        this.phoneNumberUtil = phoneNumberUtil;
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
        if (userRepository.existsById(id)){
            List<Account> userAccounts = accountRepository.findByCustomer_IdEquals(id);
            if (userAccounts.isEmpty()){
                userRepository.deleteById(id);
                return "User deleted successfully!";
            } else {
                throw new IllegalArgumentException("User has associated bank accounts and cannot be deleted");
            }
        } else {
            throw new EntityNotFoundException("User with id " + id + " does not exist.");
        }
    }


    public List<User> getAllUsers() {
       return (List<User>) userRepository.findAll();
    }



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

    private void validateDateOfBirth(String dateOfBirth) {
        try {
            //Parse date
            LocalDate date = LocalDate.parse(dateOfBirth);

            //Check if date is further than 18 years ago
            if (date.isAfter(LocalDate.now().minusYears(18)))
                throw new IllegalArgumentException("User must be at least 18 years old");

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void validatePhoneNumber(String phoneNumber){
        
        PhoneNumber number = null;
  
        try {
            //The parse method tries to parse the string into a phone number of NL type, unless a different country code is provided.
            number = phoneNumberUtil.parse(phoneNumber, "NL");
            
        }
        catch (NumberParseException e) {
            //If there's an exception while parsing the number, that means the number invalid
            throw new IllegalArgumentException("Invalid phone number");
        }

        //If the isValidNumber method returns false, that means the number is invalid
        if(!phoneNumberUtil.isValidNumber(number))
            throw new IllegalArgumentException("Invalid phone number");
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("User with email: " + email + " was not found"));
    }
}
