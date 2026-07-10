package com.genshin.aop;

import com.genshin.modules.operationlog.entity.SysOperationLog;
import com.genshin.modules.operationlog.repository.OperationLogRepository;
import com.genshin.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        String result = "success";
        String errorMsg = "";

        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Exception e) {
            result = "fail";
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            try {
                saveLog(operationLog, result, errorMsg, duration);
            } catch (Exception e) {
                log.error("Failed to save operation log", e);
            }
        }
    }

    private void saveLog(OperationLog annotation, String result, String errorMsg, long duration) {
        AuthUser authUser = getCurrentUser();
        HttpServletRequest request = getRequest();
        String ip = request != null ? request.getRemoteAddr() : "";

        var opLog = new SysOperationLog();
        opLog.setUserId(authUser != null ? authUser.getUserId() : null);
        opLog.setUsername(authUser != null ? authUser.getUsername() : "anonymous");
        opLog.setAction(annotation.action());
        opLog.setResource(annotation.resource());
        opLog.setDetail(errorMsg.isEmpty() ? annotation.detail() : errorMsg);
        opLog.setIp(ip);
        opLog.setDuration(duration);
        opLog.setResult(result);

        try {
            operationLogRepository.insert(opLog);
        } catch (Exception e) {
            log.warn("Operation: {} {} by {} ({}ms, {})",
                    opLog.getAction(), opLog.getResource(), opLog.getUsername(), duration, result);
        }
    }

    private AuthUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthUser) {
            return (AuthUser) auth.getPrincipal();
        }
        return null;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}
