package com.slipenk.clearsolutionstesttask.aspect;

import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ARGUMENT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.BEFORE_METHOD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.RESULT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.RESULT_MESSAGE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.RETURN_FROM_METHOD;

@Aspect
@Component
@Log
public class LoggingAspect {

    private static final String POINTCUT_FOR_CONTROLLER_PACKAGE = "execution(* com.slipenk.clearsolutionstesttask.controller.*.*(..))";
    private static final String POINTCUT_FOR_SERVICE_PACKAGE = "execution(* com.slipenk.clearsolutionstesttask.service.*.*(..))";
    private static final String POINTCUT_FOR_CONTROLLER_AND_SERVICE_PACKAGE = "forControllerPackage() || forServicePackage()";
    private static final String FOR_APP_FLOW = "forAppFlow()";

    @Pointcut(POINTCUT_FOR_CONTROLLER_PACKAGE)
    private void forControllerPackage() {
    }

    @Pointcut(POINTCUT_FOR_SERVICE_PACKAGE)
    private void forServicePackage() {
    }

    @Pointcut(POINTCUT_FOR_CONTROLLER_AND_SERVICE_PACKAGE)
    private void forAppFlow() {
    }

    @Before(FOR_APP_FLOW)
    public void beforeAdvice(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();

        log.info(BEFORE_METHOD + method);

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info(ARGUMENT + arg);
        }
    }

    @AfterReturning(
            pointcut = FOR_APP_FLOW,
            returning = RESULT)
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();

        log.info(RETURN_FROM_METHOD + method);
        log.info(RESULT_MESSAGE + result);
    }
}
