package com.term4.BankingAppGrp1.responseDTOs;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDTOTest {

  UserDTO validUserDTO;

  @BeforeEach
  void setUp() {
    validUserDTO = new UserDTO(
        1L,
        "123123123",
        "firstName",
        "lastName",
        LocalDate.parse("2000-01-01"),
        "0612312312",
        "email@email.com",
        true,
        1,
        1
    );
  }

  @Test
  void userDTOCreatedWithoutBsnStillCreatesAValidObject() {
    Assertions.assertDoesNotThrow(() -> new UserDTO(
        1L,
        null,
        "",
        "lastName",
        LocalDate.now(),
        "0612312312",
        "email",
        true,
        1,
        1
    ));

    UserDTO userDTO = new UserDTO(
        1L,
        null,
        "firstName",
        "lastName",
        LocalDate.now(),
        "0612312312",
        "email@email.com",
        true,
        1,
        1
    );
    Assertions.assertNotNull(userDTO);
  }


  @Test
  void id() {
    Assertions.assertEquals(1L, validUserDTO.id());
  }

  @Test
  void bsn() {
    Assertions.assertEquals("123123123", validUserDTO.bsn());
  }

  @Test
  void firstName() {
    Assertions.assertEquals("firstName", validUserDTO.firstName());
  }

  @Test
  void lastName() {
    Assertions.assertEquals("lastName", validUserDTO.lastName());
  }

  @Test
  void dateOfBirth() {
    Assertions.assertEquals(LocalDate.parse("2000-01-01"), validUserDTO.dateOfBirth());
  }

  @Test
  void phoneNumber() {
    Assertions.assertEquals("0612312312", validUserDTO.phoneNumber());
  }

  @Test
  void email() {
    Assertions.assertEquals("email@email.com", validUserDTO.email());
  }

  @Test
  void isActive() {
    Assertions.assertTrue(validUserDTO.isActive());
  }

  @Test
  void dayLimit() {
    Assertions.assertEquals(1, validUserDTO.dayLimit());
  }

  @Test
  void transactionLimit() {
    Assertions.assertEquals(1, validUserDTO.transactionLimit());
  }
}