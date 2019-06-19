package com.bridgelabz.fundoo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableScheduling
public class FundooAppApplication 
{
	public static void main(String[] args) 
	{
		System.out.println("FundooApplication.main()");
		SpringApplication.run(FundooAppApplication.class, args);
	}
}
