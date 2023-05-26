package com.term4.BankingAppGrp1.requestDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class AccountStatusDTO {
    private Boolean isActive;
    public AccountStatusDTO(Boolean isActive) {
        checkIsActiveNotNull();
        this.isActive = isActive;
    }
    public Boolean getIsActive() {
        checkIsActiveNotNull();
        return isActive;
    }
    private void checkIsActiveNotNull() {
        if (isActive == null)
            throw new IllegalArgumentException("isActive cannot be null in request body in order to update account status");
    }

}
