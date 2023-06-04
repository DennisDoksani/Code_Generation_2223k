package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO; 

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PhoneNumberUtil phoneNumberUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PhoneNumberUtil phoneNumberUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.phoneNumberUtil = phoneNumberUtil;
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User registerUser(RegistrationDTO registrationDTO) {

        validateRegistration(registrationDTO);

        User newUser = User.builder().bsn(registrationDTO.bsn())
                .firstName(registrationDTO.firstName())
                .lastName(registrationDTO.lastName())
                .dateOfBirth(LocalDate.parse(registrationDTO.dateOfBirth()))
                .phoneNumber(registrationDTO.phoneNumber())
                .email(registrationDTO.email())
                .password(bCryptPasswordEncoder.encode(registrationDTO.password()))
                .build();

        return userRepository.save(newUser);
    }

    public String deleteUser(long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
            return "User not found in the database";
        }
    }

    public User updateUser(UserUpdateDTO userUpdateDTO) {
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

    private void validateRegistration(RegistrationDTO registrationDTO) {
        //Check if email is unique
        if (userRepository.findByEmail(registrationDTO.email()).isPresent())
            throw new EntityExistsException("This e-mail is already in use.");
        //Check if BSN is unique
        if (userRepository.findByBsn(registrationDTO.bsn()).isPresent())
            throw new EntityExistsException("This BSN already in use.");

        //Validate BSN, Date of Birth, Phone number
        validateBsn(registrationDTO.bsn());
        validateDateOfBirth(registrationDTO.dateOfBirth());
        validatePhoneNumber(registrationDTO.phoneNumber());
    }

    private void validateBsn(Integer bsn) {
        //Convert Integer to String to check length
        String bsnString = bsn.toString();

        //BSN length has to be 8 or 9
        if (bsnString.length() != 9 && bsnString.length() != 8) {
            throw new IllegalArgumentException("Invalid BSN");
        }
        //If length is 8, then we have to prepend a 0 to make length 9.
        else if (bsnString.length() == 8) {
            bsnString = "0" + bsnString;
        }

        //Check valid BSN using the 11-test:
        //((9 × 1st digit) + (8 × 2nd digit) + (7 × 3rd digit) ... (2 × 8th digit) + (-1 × 9th digit)) % 11 == 0
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(bsnString.charAt(i));
            if (i == 8) {
                sum += (-1 * Character.getNumericValue(bsnString.charAt(i)));
            } else
                sum += ((9 - i) * digit);
        }

        if (sum % 11 != 0)
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

    //Validate phone number (uses Google's open-source library)
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