package com.term4.BankingAppGrp1.cucumber.auth;

import com.term4.BankingAppGrp1.cucumber.BaseStepDefinition;
import com.term4.BankingAppGrp1.requestDTOs.LoginDTO;
import io.cucumber.java.en.Given;

public class AuthStepDefinitions extends BaseStepDefinition {

    @Given("I have a valid login object with valid email and valid password")
    public void iHaveAValidLoginObjectWithValidEmailAndValidPassword() {
        LoginDTO loginDto = new LoginDTO(CUSTOMER_EMAIL, LOGIN_PASSWORD);

    }
}
