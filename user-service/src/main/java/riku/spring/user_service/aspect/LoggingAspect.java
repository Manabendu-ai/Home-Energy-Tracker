package riku.spring.user_service.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* riku.spring.user_service.service.*.*(..))")
    public void serviceMethods(){

    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint){
        log.info(
                "Called Service method : {} with arguments {}",
                joinPoint.getSignature().getName(), joinPoint.getArgs()
        );
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result){
        log.info(
                "Service method : {} returned {}",
                joinPoint.getSignature().getName(), result
        );
    }
}
