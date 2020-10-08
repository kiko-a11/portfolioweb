package jp.summit_tech.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * LogAspct is for output logs.
 *
 */
@Aspect
@Component
public class LogAspct {

    /**
     * Output logs before and after process.
     */
    @Around("@within(org.springframework.stereotype.Controller)")
    public Object startLog(ProceedingJoinPoint jp) throws Throwable {

        System.out.println(LocalDateTime.now() + "##START: " + jp.getSignature());

        try {
            Object result = jp.proceed();

            System.out.println(LocalDateTime.now() + "##END  : " + jp.getSignature());

            return result;

        } catch (Exception e) {
            System.out.println(LocalDateTime.now() + "##[ERR]END: " + jp.getSignature());
            e.printStackTrace();
            throw e;
        }
    }
}
