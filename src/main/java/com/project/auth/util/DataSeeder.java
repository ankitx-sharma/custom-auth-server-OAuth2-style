package com.project.auth.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.auth.entity.ClientEntity;
import com.project.auth.entity.UserEntity;
import com.project.auth.repository.ClientEntityRepository;
import com.project.auth.repository.UserRepository;

@Configuration
public class DataSeeder {

	@Bean
	CommandLineRunner seed(UserRepository users, ClientEntityRepository clients, PasswordEncoder encoder) {
		return args -> {
			// Seed user
			if(!users.existsByEmail("user@example.com")) {
				UserEntity user = new UserEntity();
				user.setEmail("user@example.com");
				user.setPasswordHash(encoder.encode("password"));
				user.setEnabled(true);
				users.save(user);
			}
			
			//Seed client
			if(clients.existsByClientId("my-client")) {
				ClientEntity client = new ClientEntity();
				client.setClientId("my-client");
				client.setSecretHash(encoder.encode("my-client"));
				client.setName("Sample client");
				client.setAllowedGrants("password refresh_token");
				client.setAllowedScopes("read write");
				clients.save(client);
			}
		};
	}
}
