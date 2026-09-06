package com.demoApp;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class helloApp {
	
	@GetMapping("/hello")
	public String Hello() {
		return "Hello from CI/CD Pipeline>>>>😁😁";
	}
}
