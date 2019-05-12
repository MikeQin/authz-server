package com.example.authz.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.*"})
public class AuthzServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthzServerApplication.class, args);
	}

}
