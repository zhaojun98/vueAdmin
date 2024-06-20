package com.yl.asept;

import java.lang.annotation.*;

/**
 * @author ：jerry
 * @date ：Created in 2024/5/13 09:15
 * @description：实现部门数据隔离
 * @version: V1.1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeptFilter {
}
