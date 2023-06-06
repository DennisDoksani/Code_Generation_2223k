package com.term4.BankingAppGrp1.requestDTOs;
import com.term4.BankingAppGrp1.models.InhollandIBANPattern;
import jakarta.validation.constraints.NotNull;

public record ATMDepositDTO(@InhollandIBANPattern @NotNull(message = "The account TO cannot be empty!")String accountTo,
        @NotNull(message = "The account's amount cannot be empty!") Double amount ) {


}
