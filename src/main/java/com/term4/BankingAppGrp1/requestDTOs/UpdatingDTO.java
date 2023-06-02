package com.term4.BankingAppGrp1.requestDTOs;

import com.term4.BankingAppGrp1.responseDTOs.AccountHolderDTO;
public record UpdatingDTO(double absoluteLimit,boolean isActive, AccountHolderDTO accountHolder) {
}
