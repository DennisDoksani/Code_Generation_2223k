package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.models.Role;
import com.term4.BankingAppGrp1.models.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {
        
    Optional<User> findByEmail(String email);
    Page<User> findUserByRolesEqualsAndIdNot(Pageable pageable, Role usersRole, long id);
    Page<User> findByIdNot(Pageable pageable, long id);

}
