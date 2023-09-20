package com.triply.greendrive;

import com.triply.greendrive.model.user.Role;
import com.triply.greendrive.model.user.User;
import com.triply.greendrive.model.user.dao.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GreenDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenDriveApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(UserRepository repo, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User();
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setEmail("test@gmail.com");
			user.setRole(Role.ADMIN);
			user.setPassword(passwordEncoder.encode("Test@123123"));
			repo.save(user);
		};
	}

}
