package com.bergin.moonhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.bergin.moonhive.repo")
public class MoonhiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoonhiveApplication.class, args);
	}
	
//	@Bean
//	  public WebMvcConfigurer corsConfigurer() {
//	    return new WebMvcConfigurer() {
//	      @Override
//	      public void addCorsMappings(CorsRegistry registry) {
//	       registry.addMapping("/**").allowedOrigins("http://localhost:4200")
//	                      .allowedMethods("PUT", "DELETE", "GET", "POST");
//	      }
//	    };
//	  }


}
