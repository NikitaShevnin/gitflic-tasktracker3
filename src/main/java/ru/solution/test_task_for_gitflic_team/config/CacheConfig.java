package ru.solution.test_task_for_gitflic_team.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("#{'${cache.names}'.split(',')}")
    private java.util.List<String> cacheNames;

    @Value("${cache.maximum-size}")
    private int maximumSize;

    @Value("${cache.expire-after-write-minutes}")
    private long expireAfterWriteMinutes;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                cacheNames.toArray(new String[0]));
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(Duration.ofMinutes(expireAfterWriteMinutes)));
        return manager;
    }
}