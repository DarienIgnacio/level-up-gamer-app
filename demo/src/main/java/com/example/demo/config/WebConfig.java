package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve las imágenes subidas con /uploads/filename
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/levelup-gamer-uploads/");
    }
}
