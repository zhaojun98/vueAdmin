package com.yl.asept;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：jerry
 * @date ：Created in 2024/5/13 08:58
 * @description：
 * @version: V1.1
 */
@Aspect
@Component
public class DeptAspect {

    private static ThreadLocal<List<String>> result = new ThreadLocal<>();

    /**
     * 切点定义
     */
//    @Pointcut("execution(* com.szkj.common.controller.*.*(..))")
    @Pointcut("@annotation(DeptFilter)")
    public void pointDept() {
        System.out.println();
    }

    /**
     * 切面定义
     */
    @Before("pointDept()")
    public void deptAspect(JoinPoint joinPoint) {
        System.out.println();
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> deptIdList = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        result.set(deptIdList);
    }


    /**
     * 将获取到的当前用户的部门ID与属于当前不门的所有下级部门ID返回
     */
    public List<String> getDeptList() {
        return result.get();
    }


}
