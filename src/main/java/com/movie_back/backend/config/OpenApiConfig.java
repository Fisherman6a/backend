package com.movie_back.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration // 配置类
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("影评项目 API") // 对应你之前的 title
						.description("所有 API 接口") // 对应你之前的 description
						.version("v1.0.0") // 建议添加一个版本号
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}