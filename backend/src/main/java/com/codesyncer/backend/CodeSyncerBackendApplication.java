package com.codesyncer.backend;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//remove this line when you are ready to have auth protected routes

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CodeSyncerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeSyncerBackendApplication.class, args);
	}

}
