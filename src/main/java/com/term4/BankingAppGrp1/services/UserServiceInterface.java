package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.requestDTOs.UserUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Not sure if I should mention the @Service here... and if I need to extend just like the other interfaces are.
@Service
public interface UserServiceInterface {
    String deleteUser(long id);
    Optional<User> getUser(long id);
    User updateUser(UserUpdateDTO userUpdateDTO);
}
