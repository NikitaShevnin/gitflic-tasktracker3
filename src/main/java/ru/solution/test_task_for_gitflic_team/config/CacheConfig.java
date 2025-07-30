package ru.solution.test_task_for_gitflic_team.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("tasks", "task");
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(10)));
        return manager;
    }
}