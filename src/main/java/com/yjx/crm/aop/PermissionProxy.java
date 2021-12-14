package com.yjx.crm.aop;

import com.yjx.crm.annotation.RequirePermission;
import com.yjx.crm.bean.Permission;
import com.yjx.crm.exceptions.NoLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Autowired
    private HttpSession httpSession;
    @Around(value = "@annotation(com.yjx.crm.annotation.RequirePermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        List<String> permissions = (List<String>) httpSession.getAttribute("permissions");
        if (permissions == null || permissions.size()<0){
            throw new NoLoginException();
        }
        Object result = null;
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        RequirePermission requirePermission = methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        if(!(permissions.contains(requirePermission.code()))){
            throw new NoLoginException();
        }
        result= pjp.proceed();
        return result;
    }
}
