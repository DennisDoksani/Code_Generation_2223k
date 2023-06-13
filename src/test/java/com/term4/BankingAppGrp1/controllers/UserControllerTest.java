package com.term4.BankingAppGrp1.controllers;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@WebMvcTest(controllers = UserController.class)
@Import(ApiTestConfiguration.class)
@EnableMethodSecurity
public class UserControllerTest {


}
