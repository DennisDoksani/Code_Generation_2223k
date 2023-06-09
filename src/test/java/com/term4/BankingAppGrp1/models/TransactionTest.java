package com.term4.BankingAppGrp1.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

 class TransactionTest {

  Transaction transaction = new Transaction();

  @Test
  void createNewTransactionShouldResultInAValidObject() {
    Assertions.assertNotNull(transaction);
  }

  @Test
  void setAmountToZeroOrNegativeShouldResultInIllegalArgumentException() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
        () -> transaction.setAmount(0.0));

    Assertions.assertEquals("Amount can not be zero or under", exception.getMessage());

    Exception exception2 = Assertions.assertThrows(IllegalArgumentException.class,
        () -> transaction.setAmount(-1));

    Assertions.assertEquals("Amount can not be zero or under", exception2.getMessage());
  }
}
