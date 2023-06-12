package com.term4.BankingAppGrp1.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.term4.BankingAppGrp1.models.Account;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.AccountRepository;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.RegistrationDTO;
import com.term4.BankingAppGrp1.responseDTOs.UserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AccountRepository accountRepository;
  private final PhoneNumberUtil phoneNumberUtil;

  private final int MIN_AGE_IN_YEARS = 18;
  private final String PHONE_NUMBER_REGION = "NL";

  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
      AccountRepository accountRepository, PhoneNumberUtil phoneNumberUtil) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.accountRepository = accountRepository;
    this.phoneNumberUtil = phoneNumberUtil;
  }

  public List<User> getAllUsers() {
    return (List<User>) userRepository.findAll();
  }

  public User saveUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public void saveUserWithoutHashingPassword(User user) {
    userRepository.save(user);
  }

  public User registerUser(RegistrationDTO registrationDTO) {

    validateRegistration(registrationDTO);

    User newUser = User.builder().bsn(registrationDTO.bsn())
        .firstName(registrationDTO.firstName())
        .lastName(registrationDTO.lastName())
        .dateOfBirth(LocalDate.parse(registrationDTO.dateOfBirth()))
        .phoneNumber(registrationDTO.phoneNumber())
        .email(registrationDTO.email().toLowerCase())
        .password(bCryptPasswordEncoder.encode(registrationDTO.password()))
        .build();

    return userRepository.save(newUser);
  }

  public String deleteUser(long id) {
    if (userRepository.existsById(id)) {
      List<Account> userAccounts = accountRepository.findByCustomer_IdEquals(id);
      if (userAccounts.isEmpty()) {
        userRepository.deleteById(id);
        return "User deleted successfully!";
      } else {
        throw new IllegalArgumentException(
            "User has associated bank accounts and cannot be deleted");
      }
    } else {
      throw new EntityNotFoundException("User with id " + id + " does not exist.");
    }
  }

  public User updateUser(long id, UserDTO userDTO) {
    User updatingUser = userRepository.findById(id)
            .orElseThrow(
                    () -> new EntityNotFoundException("The updating user with ID: " + userDTO.id() + " was not found"));

    updatingUser.setFirstName(userDTO.firstName());
    updatingUser.setLastName(userDTO.lastName());
    validateDateOfBirth(String.valueOf(userDTO.dateOfBirth()));
    validatePhoneNumber(String.valueOf(userDTO.phoneNumber()));
    updatingUser.setEmail(userDTO.email());
    updatingUser.setActive(userDTO.isActive());
    updatingUser.setDayLimit(userDTO.dayLimit());
    updatingUser.setTransactionLimit(userDTO.transactionLimit());

    return userRepository.save(updatingUser);
  }

  public User getUser(long id) {
    return userRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("User with id: " + id + " was not found"));
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmailEqualsIgnoreCase(email).orElseThrow(() ->
        new EntityNotFoundException("User with email: " + email + " was not found"));
  }

  private void validateRegistration(RegistrationDTO registrationDTO) {
    //Check if email is unique
      if (userRepository.findByEmail(registrationDTO.email()).isPresent()) {
          throw new EntityExistsException("This e-mail is already in use.");
      }
    //Check if BSN is unique
      if (userRepository.findByBsn(registrationDTO.bsn()).isPresent()) {
          throw new EntityExistsException("This BSN already in use.");
      }

    //Validate BSN, Date of Birth, Phone number
    validateBsn(registrationDTO.bsn());
    validateDateOfBirth(registrationDTO.dateOfBirth());
    validatePhoneNumber(registrationDTO.phoneNumber());
  }

  private void validateBsn(String bsnString) {
    //Check if numeric
      if (!bsnString.matches("[0-9]+")) {
          throw new IllegalArgumentException("BSN must be numeric.");
      }

    //BSN length has to be 8 or 9
    if (bsnString.length() != 9 && bsnString.length() != 8) {
      throw new IllegalArgumentException("Invalid BSN length.");
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
        } else {
            sum += ((9 - i) * digit);
        }
    }

      if (sum % 11 != 0) {
          throw new IllegalArgumentException("BSN is invalid.");
      }
  }

  private void validateDateOfBirth(String dateOfBirth) {
    try {
      //Parse date
      LocalDate date = LocalDate.parse(dateOfBirth);

      //Check if date is further than 18 years ago
        if (date.isAfter(LocalDate.now().minusYears(MIN_AGE_IN_YEARS))) {
            throw new IllegalArgumentException(
                ("User must be at least " + MIN_AGE_IN_YEARS + " years old."));
        }

    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private void validatePhoneNumber(String phoneNumber) {

    PhoneNumber number = null;

    try {
      //The parse method tries to parse the string into a phone number of NL type, unless a different country code is provided.
      number = phoneNumberUtil.parse(phoneNumber, PHONE_NUMBER_REGION);

    } catch (NumberParseException e) {
      //If there's an exception while parsing the number, that means the number invalid
      throw new IllegalArgumentException("Phone number is invalid.");
    }

    //If the isValidNumber method returns false, that means the number is invalid
    if (!phoneNumberUtil.isValidNumber(number))
      throw new IllegalArgumentException("Phone number is invalid.");
  }


}