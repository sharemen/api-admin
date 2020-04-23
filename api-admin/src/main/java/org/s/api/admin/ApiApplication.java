package org.s.api.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;



@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
@SpringBootApplication
@EnableApolloConfig
@ComponentScan(basePackages = { "org.s.api.admin","org.s.api.admin.service" })
public class ApiApplication {

	public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
	}

}