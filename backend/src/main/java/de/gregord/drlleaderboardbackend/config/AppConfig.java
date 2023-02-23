package de.gregord.drlleaderboardbackend.config;

import org.cache2k.expiry.ExpiryPolicy;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableCaching
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
    public CacheManager cacheManager() {
        return new SpringCache2kCacheManager()
                .addCache(c -> c.name("overallranking")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true)
                        .weigher((k, v) -> {
                            if (v instanceof Collection) {
                                return ((Collection) v).size();
                            }
                            return 1;
                        })
                        .maximumWeight(50 * 15))
                .addCache(c -> c.name("tracks")
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("parentCategories")
                        .eternal(true)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("leaderboardbyplayername")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(200)
                        .permitNullValues(true))
                .addCache(c -> c.name("leaderboardbytrack")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(200)
                        .permitNullValues(true))
                .addCache(c -> c.name("latestLeaderboardActivity")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("latestLeaderboardActivityTop10")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("mostPbsLast7Days")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("mostPbsLastMonth")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("mostEntriesByTrackLast14Days")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                .addCache(c -> c.name("mostEntriesByTrackLastMonth")
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .entryCapacity(1)
                        .permitNullValues(true))
                ;
    }

}
