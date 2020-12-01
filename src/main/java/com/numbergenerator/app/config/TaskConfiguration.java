package com.numbergenerator.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;


@Configuration
@ConfigurationProperties(prefix = "task-configuration")
@Getter
@Setter
public class TaskConfiguration {
    @NonNull
    Integer delayInSeconds;
}
