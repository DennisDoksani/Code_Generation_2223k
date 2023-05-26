package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import com.term4.BankingAppGrp1.util.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.awt.print.Pageable;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public String deleteUser(long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } else {
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
        updatingUser.setPassword(userUpdateDTO.password());
        //setActive or setIsActive??
        updatingUser.setActive(userUpdateDTO.isActive());
        //setActive or setIsActive??
        updatingUser.setDayLimit(userUpdateDTO.dayLimit());
        updatingUser.setTransactionLimit(userUpdateDTO.transactionLimit());

        return userRepository.save(updatingUser);
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
