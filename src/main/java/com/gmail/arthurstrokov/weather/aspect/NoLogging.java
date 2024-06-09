package com.gmail.arthurstrokov.weather.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 28.09.2022
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLogging {
}
