package com.phucx.phucxfoodshop.aspects;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@Aspect
@Component
public class LoggerAspect {
    @Around("execution(* com.phucx.phucxfoodshop.service.*.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString() + " method has been invoked");
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant endTime = Instant.now();
        Long durationMilis = Duration.between(startTime, endTime).toMillis();
        log.info("Time took: " + durationMilis +" ms to execute");
        log.info(joinPoint.getSignature().toString() + " has ended");
        return result;
    }

    @Around("execution(* com.phucx.phucxfoodshop.controller.ExceptionController.*(..))")
    public Object exceptionLoggerAspect(ProceedingJoinPoint joinPoint) throws Throwable{

        for(Object ex: joinPoint.getArgs()){
            if(ex instanceof Exception){
                Exception exception = (Exception) ex;
                log.warn(joinPoint.getSignature().toString() + " has an Error: " + exception.getMessage());
            }
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(com.phucx.phucxfoodshop.annotations.LoggerAspect)")
    public Object logAnnotation(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString() + " method has been invoked");
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant endTime = Instant.now();
        Long durationMilis = Duration.between(startTime, endTime).toMillis();
        log.info("Time took: " + durationMilis +" ms to execute");
        log.info(joinPoint.getSignature().toString() + " has ended");
        return result;
    }
}
