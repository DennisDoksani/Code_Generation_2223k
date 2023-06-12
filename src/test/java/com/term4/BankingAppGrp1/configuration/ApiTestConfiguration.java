package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.util.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class ApiTestConfiguration {

  @MockBean
  private JwtTokenProvider jwtTokenProvider;
}
