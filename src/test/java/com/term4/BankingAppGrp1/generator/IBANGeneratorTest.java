package com.term4.BankingAppGrp1.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.term4.BankingAppGrp1.generators.IBANGenerator;
import com.term4.BankingAppGrp1.models.Account;
import java.io.Serializable;
import java.util.Random;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class IBANGeneratorTest {

  @Mock
  private SharedSessionContractImplementor sharedSessionContractImplementor;

  @Test
  void generate_ShouldReturnExistingIban_WhenAccountHasIban() {
    // Arrange
    MockitoAnnotations.openMocks(this);
    Account account = new Account();
    account.setIban("NL21INHO0123400081");
    IBANGenerator generator = new IBANGenerator(new Random());

    // Act
    Serializable generatedIban = generator.generate(sharedSessionContractImplementor, account);

    // Assert
    assertEquals("NL21INHO0123400081", generatedIban);
  }


}
