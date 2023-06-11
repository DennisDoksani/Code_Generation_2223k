package com.term4.BankingAppGrp1.responseDTOs;

import jakarta.validation.constraints.NotBlank;

public record SearchingAccountDTO(
        String iban,
        String accountHolderName){ 

}
