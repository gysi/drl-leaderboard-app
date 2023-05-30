package de.gregord.drlleaderboardbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.type.FormatMapper;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.jackson.JacksonJsonFormatMapper;

/**
 * The default Hibernate json_formatter does not support Java 8 time types.
 * This class here to supports them.
 * It is configured in application.yml (spring.jpa.properties.hibernate.type.json_format_mapper)
 */
public class CustomHibernateJsonFormatter implements FormatMapper {
    private final FormatMapper delegate;

    public CustomHibernateJsonFormatter(){
        this.delegate = new JacksonJsonFormatMapper(createObjectMapper());
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.fromString(charSequence, javaType, wrapperOptions);
    }

    @Override
    public <T> String toString(T value, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.toString(value, javaType, wrapperOptions);
    }
}
