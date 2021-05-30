package ru.yan0kom.innfl.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ru.yan0kom.innfl")
@EnableJpaRepositories("ru.yan0kom.innfl.dao")
public class InnflApplicationConfig {
}
