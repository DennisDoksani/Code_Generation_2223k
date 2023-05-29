package com.term4.BankingAppGrp1.responseDTOs;

import com.term4.BankingAppGrp1.models.Role;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(long id, long bsn, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, List<Role> roles) {
}
