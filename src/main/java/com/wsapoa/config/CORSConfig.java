package com.wsapoa.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**").
                        allowedOrigins("http://localhost:3000","http://192.168.20.66:3000").
                        allowedMethods("GET", "POST", "PUT", "DELETE").
                        allowedHeaders("*").
                        allowCredentials(true);
            }
        };
    }
}
