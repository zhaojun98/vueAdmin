package com.yl.controller;


import com.yl.common.CommonResultVo;
import com.yl.common.log.MyLog;
import com.yl.model.dto.PassDto;
import com.yl.model.entity.User;
import com.yl.service.RoleService;
import com.yl.service.UserRoleService;
import com.yl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：用户
 * @version: V1.1
 */

@Api(tags = "用户管理")
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    UserService sysUserService;

    @Autowired
    RoleService sysRoleService;

    @Autowired
    UserRoleService sysUserRoleService;
//
//    @Autowired
//    MyPasswordEncoder passwordEncoder;

    @ApiOperation("用户id查询用户信息")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public CommonResultVo info(@PathVariable("id") Long id) {
        return CommonResultVo.success( sysUserService.info(id));
    }


    @ApiOperation("分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public CommonResultVo pageList(User user) {
        return CommonResultVo.success(sysUserService.pageList(user));
    }

    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public CommonResultVo saveObj(@Validated @RequestBody User sysUser) {
        return CommonResultVo.success(  sysUserService.saveObj(sysUser));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public CommonResultVo update(@Validated @RequestBody User sysUser) {
        return CommonResultVo.success(sysUserService.updateObj(sysUser));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public CommonResultVo delete(@RequestBody Long[] ids) {
        return CommonResultVo.success(sysUserService.delete(ids));
    }

    @ApiOperation("根据用户id删除")
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public CommonResultVo rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        return CommonResultVo.success(sysUserService.rolePerm(userId,roleIds));
    }

    @ApiOperation("重置密码")
    @MyLog(value = "重置密码")
    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public CommonResultVo repass(@RequestBody Long userId) {
        return CommonResultVo.success(sysUserService.repass(userId));
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePass")
    public CommonResultVo updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {
        sysUserService.updatePass(passDto,principal);
        return CommonResultVo.success("");
    }
}
