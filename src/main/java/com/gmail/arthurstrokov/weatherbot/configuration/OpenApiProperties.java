package com.gmail.arthurstrokov.weatherbot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author Arthur Strokov
 * @email arthurstrokov@gmail.com
 * @created 24.09.2022
 */
@Configuration
@PropertySource("classpath:values.properties")
public class OpenApiProperties {

    private final Environment environment;
    private String url;
    private String key;

    @Autowired
    public OpenApiProperties(Environment environment) {
        this.environment = environment;
    }

    /**
     * Spring calls the methods annotated with @PostConstruct only once,
     * just after the initialization of bean properties.
     */
    @PostConstruct
    public void initialize() {
        this.url = environment.getProperty("open.api.url");
        this.key = environment.getProperty("open.api.key");
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }
}
