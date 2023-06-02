package com.term4.BankingAppGrp1;

import com.term4.BankingAppGrp1.models.ConstantsContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingAppGrp1Application implements ConstantsContainer {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppGrp1Application.class, args); 
	}
}
