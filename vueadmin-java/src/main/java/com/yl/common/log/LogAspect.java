package com.yl.common.log;


import com.alibaba.fastjson.JSON;
import com.yl.model.entity.Log;
import com.yl.service.LogService;
import com.yl.utils.IpUtiles;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 切面设计
 * */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private LogService logService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(MyLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) throws Exception {

            //保存日志
            Log sysLog = new Log();

            //从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();

            //获取操作
            MyLog myLog = method.getAnnotation(MyLog.class);
            if (null!=myLog) {
                String value = myLog.value();
                sysLog.setOperation(value);//保存获取的操作
            }

            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            //获取请求的方法名
            String methodName = method.getName();
            sysLog.setMethod(className + "." + methodName);

            sysLog.setCreateTime(LocalDateTime.now());
            //获取请求
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();

            //获取账号登陆账号信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            //获取用户名
            String principal = (String) authentication.getPrincipal();
            sysLog.setUsername(principal);
            //获取用户ip地址
            sysLog.setIp(IpUtiles.getRealIp(request));
            //请求的参数
            Object[] args = joinPoint.getArgs();
            //将参数所在的数组转换成json
            String params = JSON.toJSONString(args);
            sysLog.setParams(params);
            //调用service保存SysLog实体类到数据库
            logService.save(sysLog);

    }

}