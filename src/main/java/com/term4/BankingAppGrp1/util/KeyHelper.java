package com.term4.BankingAppGrp1.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.security.Key;
import java.security.KeyStore;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeyHelper {

    private KeyHelper() {
        //Private constructor to hide the implicitely public one for a class with only static methods
    }
    
    public static Key getPrivateKey(String alias, String keystore, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Resource resource = new ClassPathResource(keystore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(resource.getInputStream(), password.toCharArray());

        return keyStore.getKey(alias, password.toCharArray());
    }
    
}
