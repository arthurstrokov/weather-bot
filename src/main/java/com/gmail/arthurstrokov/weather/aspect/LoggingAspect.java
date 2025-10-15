package com.gmail.arthurstrokov.weather.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 27.09.2022
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(com.gmail.arthurstrokov.weather.controller..*) || within(com.gmail.arthurstrokov.weather.service..*)")
    public void applicationPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("execution(* com.gmail.arthurstrokov.weather.service.*.*(..))" +
            "&& !@annotation(com.gmail.arthurstrokov.weather.aspect.NoLogging)" +
            "&& !@target(com.gmail.arthurstrokov.weather.aspect.NoLogging)")
    public void servicePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("execution(* com.gmail.arthurstrokov.weather.controller.*.*(..))")
    public void controllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @AfterThrowing(pointcut = "applicationPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) {
        return logAround(joinPoint);
    }

    @Around("controllerPointcut()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) {
        return logAround(joinPoint);
    }

    @SneakyThrows
    private Object logAround(ProceedingJoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @SneakyThrows
    @Around("applicationPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("{} executed in {} ms", joinPoint.getSignature(), stopWatch.getTotalTimeMillis());
        }
    }
}
