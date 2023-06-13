package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import com.term4.BankingAppGrp1.testingData.BankingAppTestData;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//making isolated for each tests
@Import(ApiTestConfiguration.class)
public abstract class BaseRepositoryTest extends BankingAppTestData {

  @Autowired
  protected UserRepository userRepository;

  @BeforeEach
  protected void setupData() {
    super.setupData();
  }


}
