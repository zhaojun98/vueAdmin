package com.yl.controller;


import cn.hutool.core.lang.tree.Tree;
import com.yl.common.CommonResultVo;
import com.yl.model.entity.Menu;
import com.yl.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：菜单
 * @version: V1.1
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Resource
    MenuService sysMenuService;


    @ApiOperation("当前用户的菜单和权限信息")
    @GetMapping("/nav")
    public CommonResultVo nav(Principal principal) {
        return CommonResultVo.success(sysMenuService.nav(principal));
    }

    @ApiOperation("根据菜单Id查询详情")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public CommonResultVo info(@PathVariable(name = "id") Long id) {
        return CommonResultVo.success(sysMenuService.getById(id));
    }

    @ApiOperation("列表查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public CommonResultVo<List<Tree<String>>> list() {
        return CommonResultVo.success(sysMenuService.tree());
    }

    @ApiOperation("新增")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public CommonResultVo save(@Validated @RequestBody Menu sysMenu) {
        return CommonResultVo.success(sysMenuService.saveObj(sysMenu));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public CommonResultVo update(@Validated @RequestBody Menu sysMenu) {
        return CommonResultVo.success(sysMenuService.updateObj(sysMenu));
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public CommonResultVo delete(@PathVariable("id") Long id) {
        return CommonResultVo.success(sysMenuService.delete(id));
    }
}
