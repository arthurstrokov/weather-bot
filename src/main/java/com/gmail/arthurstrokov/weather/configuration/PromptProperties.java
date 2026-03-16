package com.gmail.arthurstrokov.weather.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptProperties {

    private final MessageSource messageSource;

    public String getTemplate() {
        return messageSource.getMessage(
                "prompt.template",
                new Object[]{},
                LocaleContextHolder.getLocale()
        );
    }
}