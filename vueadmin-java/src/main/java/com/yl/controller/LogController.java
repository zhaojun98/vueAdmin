package com.yl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.common.lang.Result;
import com.yl.entity.Log;
import com.yl.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：系统日志
 * @version: V1.1
 */

@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询
     * */
    @PostMapping("/findList")
    @PreAuthorize("hasAuthority('sys:log:findList')")
    public Result findList(@RequestBody Log log) {
        QueryWrapper<Log> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        Page<Log> page = logService.page(new Page<>(log.getCurrent(), log.getSize()),qw);
        return Result.succ(page);
    }
}
