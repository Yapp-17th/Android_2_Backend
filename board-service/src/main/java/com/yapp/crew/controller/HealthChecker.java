package com.yapp.crew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Chatting Controller - Health Checker")
@RestController
public class HealthChecker {

	@GetMapping(path = "/health-check")
	public String healthCheck() {
		log.info("Health check...");
		return "healthy";
	}
}
