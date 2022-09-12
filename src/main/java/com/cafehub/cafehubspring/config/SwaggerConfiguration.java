package com.cafehub.cafehubspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * http://localhost:8080/swagger-ui/#/
 */

@Configuration
public class SwaggerConfiguration {

    /**
     * swagger 문서 api 설정
     */
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false) // Swagger에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false로 설정하면 기본 응답 코드를 노출하지 않습니다.
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cafehub.cafehubspring.controller")) // Basic error controller 를 없애기 위한 코드
                .apis(RequestHandlerSelectors.any()) // Swagger를 적용할 패키지 설정
                .paths(PathSelectors.any()) // Swagger를 적용할 주소 패턴을 세팅
                .build()
                .apiInfo(apiInfo()); // Swagger UI 로 노출할 정보
    }


    /**
     * api 정보 설정 부분
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cafehub API")
                .description("cafehub Swagger DOC")
                .version("1.0")
                .build();
    }
}
