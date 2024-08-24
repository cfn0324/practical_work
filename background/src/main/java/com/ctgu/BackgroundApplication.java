package com.ctgu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ctgu.mapper")
public class BackgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackgroundApplication.class, args);
	}

}