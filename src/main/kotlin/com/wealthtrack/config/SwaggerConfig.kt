package com.wealthtrack.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }

    private fun apiInfo(): Info {
        return Info()
            .title("wealthtrack Financial Dashboard API")
            .description("RESTful API for wealthtrack financial dashboard application")
            .version("1.0.0")
            .contact(
                Contact()
                    .name("wealthtrack Team")
                    .email("info@wealthtrack.com")
                    .url("https://wealthtrack.com")
            )
            .license(
                License()
                    .name("Private License")
                    .url("https://wealthtrack.com/license")
            )
    }
}
