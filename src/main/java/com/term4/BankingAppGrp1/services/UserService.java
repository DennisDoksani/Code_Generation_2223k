package com.term4.BankingAppGrp1.services;

import com.term4.BankingAppGrp1.models.User;
import com.term4.BankingAppGrp1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(User id) {
        //supposedly the exist and deleteById are methods provided by the Spring Data JPA framework right? Why this error then?
        //Solved problem by adding id.getId(). Correct?

        if (userRepository.existsById(id.getId())){
            userRepository.deleteById(id.getId());
            return "User deleted successfully";
        } else {
            return "User not found in the database";
        }
    }

    @Override
    public Optional<User> getUser(User id) {
        return userRepository.findById(id.getId());
    }

}
