package com.yl.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.asept.DeptFilter;
import com.yl.common.CommonResultVo;
import com.yl.model.entity.Role;
import com.yl.service.RoleMenuService;
import com.yl.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：角色
 * @version: V1.1
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    RoleService sysRoleService;

    @Resource
    RoleMenuService sysRoleMenuService;


    @ApiOperation("根据角色id查询菜单")
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public CommonResultVo<Role> info(@PathVariable("id") Long id) {
        return CommonResultVo.success(sysRoleService.info(id));
    }

    @DeptFilter
    @ApiOperation("根据角色名查询")
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public CommonResultVo<Page<Role>> pageList(Role role) {
        return CommonResultVo.success(sysRoleService.pageList(role));
    }

    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public CommonResultVo<Role> saveObj(@Validated @RequestBody Role sysRole) {
        return CommonResultVo.success(sysRoleService.saveObj(sysRole));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public CommonResultVo<Role> updateObj(@Validated @RequestBody Role sysRole) {
        return CommonResultVo.success(sysRoleService.updateObj(sysRole));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public CommonResultVo delete(@RequestBody Long[] ids) {
        sysRoleService.delete(ids);
        return CommonResultVo.success();
    }


    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public CommonResultVo info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        return CommonResultVo.success( sysRoleMenuService.info(roleId,menuIds));
    }

}
