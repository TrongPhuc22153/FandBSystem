package com.phucx.phucxfandb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan("com.phucx.phucxfandb.entity")
@EnableJpaRepositories("com.phucx.phucxfandb.repository")
public class PhucxfandbApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhucxfandbApplication.class, args);
	}

}
