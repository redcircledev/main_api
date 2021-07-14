package com.rcdev.main_api;

import org.assertj.core.util.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class MainApiApplication {

    //Please note that Swagger interface is on http://localhost:8080/swagger-ui/

    public static void main(String[] args) {
        SpringApplication.run(MainApiApplication.class, args);
    }

    @Bean
    public Docket swaggerConfiguration() {
        // Return a prepared Docket instance
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.rcdev.main_api"))
                .build()
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiDetails());

    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer",
                authorizationScopes));
    }

    private ApiInfo apiDetails(){
        return new ApiInfo(
                "Main Testing API",
                "Main API used to do testing for rcdev web projects",
                "1.0",
                "RCDEV assosiates only",
                new springfox.documentation.service.Contact("Iván Alcázar", "http://rcdev.com","testingemail@rcdev.com"),
                "API License",
                "http://rcdev.com",
                Collections.emptyList());
    }

}
