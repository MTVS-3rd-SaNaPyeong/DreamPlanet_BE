package com.sanapyeong.mtvs_3rd_dreamplanet.component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.sanapyeong.mtvs_3rd_dreamplanet..controller..*.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 메소드 정보 추출
        Method method = getMethod(proceedingJoinPoint);

        log.info("===== method name = {} =====", method.getName());

        // 파라미터 추출
        Object[] args = proceedingJoinPoint.getArgs();

        if (args.length == 0) {
            log.info("No Parameter");
        }

        for (Object arg : args) {
            log.info("Parameter Type = {}", arg.getClass().getSimpleName());
            log.info("Parameter Value = {}", arg);
        }

        // 실제 메소드 실행
        Object response = proceedingJoinPoint.proceed(args);

        log.info("Response Type = {}", response.getClass().getSimpleName());
        log.info("Response Value = {}", response);

        if (response instanceof ResponseEntity<?> responseEntity) {

            Object body = responseEntity.getBody();

            if (body == null) {
                return response;
            }

            log.info("Response Body Type = {}", body.getClass().getSimpleName());
            log.info("Response Body Value = {}", body);
        }

        return response;
    }

    // Method 정보 추출
    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        return methodSignature.getMethod();
    }
}
