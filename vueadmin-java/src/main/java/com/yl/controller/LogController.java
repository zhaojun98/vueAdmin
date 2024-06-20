package com.yl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yl.common.CommonResultVo;
import com.yl.model.entity.Log;
import com.yl.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询
     * */
    @ApiOperation("列表分页查询")
    @PostMapping("/findList")
    @PreAuthorize("hasAuthority('sys:log:findList')")
    public CommonResultVo pageList(@RequestBody Log log) {
        return CommonResultVo.success(logService.pageList(log));
    }
}
