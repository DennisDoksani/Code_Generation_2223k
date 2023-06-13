package com.term4.BankingAppGrp1.repositories;

import com.term4.BankingAppGrp1.configuration.ApiTestConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//making issolated for each tests
@Import(ApiTestConfiguration.class)
public abstract class BaseRepositoryTest {
  

}
