package com.gmail.arthurstrokov.weather.configuration;

import com.gmail.arthurstrokov.weather.tool.WeatherTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ToolConfiguration {

    @Bean
    public ToolCallbackProvider localToolProvider(WeatherTool weatherTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(weatherTool)
                .build();
    }

    @Bean
    public ToolCallback[] toolCallbacks(ToolCallbackProvider provider) {
        return provider.getToolCallbacks();
    }
}
