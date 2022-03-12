package com.example.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HelperApplication {

//	@Bean
//	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//		threadPoolTaskScheduler.setPoolSize(5);
//		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
//		return threadPoolTaskScheduler;
//	}

//	@Bean
//	public CaptchaGenerator getCaptchaGenerator() {
//		return new CaptchaGenerator();
//	}

	public static void main(String[] args) {
		SpringApplication.run(HelperApplication.class, args);
	}

}
