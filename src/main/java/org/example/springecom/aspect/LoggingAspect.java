package org.example.springecom.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Pointcut for all service methods
    @Pointcut("execution(* org.example.springecom.service..*(..))")
    public void serviceMethods() {}

    // Pointcut for all controller methods
    @Pointcut("execution(* org.example.springecom.controller..*(..))")
    public void controllerMethods() {}

    // Combined pointcut for both controller and service methods
    @Pointcut("serviceMethods() || controllerMethods()")
    public void appMethods() {}

    @Before("appMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("🔹 Entering: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "appMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("✅ Completed: {} with result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "appMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("❌ Exception in: {} with message: {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }

    @Around("appMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            logger.debug("🕒 {} executed in {} ms", joinPoint.getSignature(), duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            logger.error("🕒 {} failed after {} ms", joinPoint.getSignature(), duration);
            throw ex;
        }
    }
}
