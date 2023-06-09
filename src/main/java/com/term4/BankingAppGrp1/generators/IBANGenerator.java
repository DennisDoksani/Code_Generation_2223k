package com.term4.BankingAppGrp1.generators;

import com.term4.BankingAppGrp1.models.Account;
import java.io.Serializable;
import java.util.Random;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class IBANGenerator implements IdentifierGenerator {

  private final Random randomizer; // taking from bean from beanFactory which is already made

  public IBANGenerator(Random randomizer) {
    this.randomizer = randomizer;
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o) throws HibernateException {
    if (o instanceof Account account && account.getIban() != null) {
      return account.getIban();
    } else {
      return generateIban();
    }
  }

  private String generateIban() {
    // Generate random number for account number
    long accountNumber = randomizer.nextLong();

    // Format account number to 9 digits
    String formattedAccountNumber = String.valueOf(Math.abs(accountNumber)).substring(0, 9);

    // Generate random digits for "xx" portion
    int randomDigits = randomizer.nextInt(100);

    // Format random digits to 2 digits
    String formattedRandomDigits = String.format("%02d", randomDigits);

    // Generate IBAN with NLxxINHO0 prefix and random values
    return "NL" + formattedRandomDigits + "INHO0" + formattedAccountNumber;
  }
}
