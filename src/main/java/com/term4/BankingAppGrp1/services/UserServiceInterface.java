package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import org.springframework.stereotype.Service;

//Not sure if I should mention the @Service here... and if I need to extend just like the other interfaces are.
@Service
public interface UserServiceInterface {
    String deleteUser(User id);
}
