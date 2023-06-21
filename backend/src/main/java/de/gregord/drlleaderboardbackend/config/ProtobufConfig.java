package de.gregord.drlleaderboardbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class ProtobufConfig {

    @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufJsonFormatHttpMessageConverter();
    }
}
