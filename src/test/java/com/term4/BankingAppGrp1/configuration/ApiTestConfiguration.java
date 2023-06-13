package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.util.JwtTokenProvider;
import java.util.Random;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ApiTestConfiguration {

  @MockBean
  private JwtTokenProvider jwtTokenProvider;
  @Bean
  public Random random() {
    return new Random();
  }
}
