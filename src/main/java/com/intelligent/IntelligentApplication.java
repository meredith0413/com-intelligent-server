package com.intelligent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.intelligent.mapper")
public class IntelligentApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentApplication.class, args);
	}

}
