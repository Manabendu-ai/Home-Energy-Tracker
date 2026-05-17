package riku.spring.device_service.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Pointcut("execution(* riku.spring.device_service.controller.*.*(..))")
    public void controllerMethods(){}


    @Around("controllerMethods()")
    public Object measureExeTime(ProceedingJoinPoint pjp) throws Throwable{
        long start = System.currentTimeMillis();
        try{
            return pjp.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long span = end-start;
            String sig = pjp.getSignature().toShortString();
            log.info(
                    "Controller Method: {}, Execution Time: {}ms",
                    sig, span
            );
        }
    }


}
