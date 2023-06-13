package com.term4.BankingAppGrp1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.term4.BankingAppGrp1.models.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserRepositoryTest extends BaseRepositoryTest {

  @BeforeEach
  protected void setupData() {
    super.setupData();
  }

  @Test
  void findByEmailShouldReturnAValidUserIfContainsInDb() {
    User savedUser = userRepository.save(customerUser);
    Optional<User> findingUser = userRepository.findByEmail(customerUser.getEmail());
    assert (findingUser.isPresent());
    assert (findingUser.get().getEmail().equals(customerUser.getEmail()));
    assertEquals(savedUser, findingUser.get());
  }

  @Test
  void findByEmailShouldReturnEmptyIfNotContainsInDb() {
    Optional<User> findingUser = userRepository.findByEmail(customerUser.getEmail());
    assertTrue(findingUser.isEmpty());
  }

  @Test
  void findByEmailEqualsIgnoreCaseShouldReturnAValidUserIfContainsInDb() {
    User savedUser = userRepository.save(customerUser);
    Optional<User> findingUser = userRepository.findByEmailEqualsIgnoreCase(
        customerUser.getEmail().toUpperCase());
    assert (findingUser.isPresent());
    assert (findingUser.get().getEmail().equals(customerUser.getEmail()));
    assertEquals(savedUser, findingUser.get());
  }

  @Test
  void saveUserShouldSaveTheUserInDb() {
    User savedUser = userRepository.save(customerUser);
    Optional<User> findingUser = userRepository.findById(savedUser.getId());
    assert (findingUser.isPresent());
    assertEquals(savedUser, findingUser.get());
  }

  @Test
  void findByBsnShouldReturnAValidUserIfContainsInDb() {
    User savedUser = userRepository.save(customerUser);
    Optional<User> findingUser = userRepository.findByBsn(customerUser.getBsn());
    assert (findingUser.isPresent());
    assert (findingUser.get().getBsn().equals(customerUser.getBsn()));
    assertEquals(savedUser, findingUser.get());
  }

  @Test
  void findByBsnShouldReturnEmptyIfNotContainsInDb() {
    Optional<User> findingUser = userRepository.findByBsn(customerUser.getBsn());
    assertTrue(findingUser.isEmpty());
  }


}
