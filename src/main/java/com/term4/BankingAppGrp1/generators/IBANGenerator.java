package com.term4.BankingAppGrp1.generators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Random;

public class IBANGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        String iban = generateIban();

        return iban;
    }

    private String generateIban() {
        // Generate random number for account number
        Random random = new Random();
        long accountNumber = random.nextLong();

        // Format account number to 10 digits
        String formattedAccountNumber = String.format("%010d", Math.abs(accountNumber));

        // Generate random digits for "xx" portion
        int randomDigits = random.nextInt(100);

        // Format random digits to 2 digits
        String formattedRandomDigits = String.format("%02d", randomDigits);

        // Generate IBAN with NLxxINHO0 prefix and random values
        String iban = "NL" + formattedRandomDigits + "INHO0" + formattedAccountNumber;

        return iban;
    }
}
