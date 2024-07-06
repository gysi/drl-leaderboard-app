package de.gregord.drlleaderboardbackend.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.gregord.drlleaderboardbackend.domain.Season;
import de.gregord.drlleaderboardbackend.domain.serializer.SeasonSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Bean
    public SimpleModule seasonModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Season.class, new SeasonSerializer(Season.class));
        return module;
    }
}
