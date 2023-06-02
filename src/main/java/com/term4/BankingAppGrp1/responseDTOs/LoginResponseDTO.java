package com.term4.BankingAppGrp1.responseDTOs;

public record LoginResponseDTO(String jwt, long id, String email, String name) {
    
}
