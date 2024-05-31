package com.javacode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.javacode.mapper")
public class OldpeopleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OldpeopleApplication.class, args);
	}

}
