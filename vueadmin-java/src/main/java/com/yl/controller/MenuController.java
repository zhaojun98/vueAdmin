package com.yl.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.common.dto.SysMenuDto;
import com.yl.common.lang.Result;
import com.yl.entity.Menu;
import com.yl.entity.RoleMenu;
import com.yl.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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
public class MenuController extends BaseController {

	/**
	 * 用户当前用户的菜单和权限信息
	 * @param principal
	 * @return
	 */
	@ApiOperation("用户当前用户的菜单和权限信息")
	@GetMapping("/nav")
	public Result nav(Principal principal) {
		User sysUser = sysUserService.getByUsername(principal.getName());

		// 获取权限信息
		String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());// ROLE_admin,ROLE_normal,sys:user:list,....
		String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

		// 获取导航栏信息
		List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();

		return Result.succ(MapUtil.builder()
				.put("authoritys", authorityInfoArray)
				.put("nav", navs)
				.map()
		);
	}

	@ApiOperation("根据菜单Id查询详情")
	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('sys:menu:list')")
	public Result info(@PathVariable(name = "id") Long id) {
		return Result.succ(sysMenuService.getById(id));
	}

	@ApiOperation("列表查询")
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('sys:menu:list')")
	public Result list() {

		List<Menu> menus = sysMenuService.tree();
		return Result.succ(menus);
	}

	@ApiOperation("新增")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('sys:menu:save')")
	public Result save(@Validated @RequestBody Menu sysMenu) {

		sysMenu.setCreateTime(LocalDateTime.now());

		sysMenuService.save(sysMenu);
		return Result.succ(sysMenu);
	}

	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('sys:menu:update')")
	public Result update(@Validated @RequestBody Menu sysMenu) {

		sysMenu.setUpdateTime(LocalDateTime.now());

		sysMenuService.updateById(sysMenu);

		// 清除所有与该菜单相关的权限缓存
		sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
		return Result.succ(sysMenu);
	}

	@ApiOperation("删除")
	@PostMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('sys:menu:delete')")
	public Result delete(@PathVariable("id") Long id) {

		int count = sysMenuService.count(new QueryWrapper<Menu>().eq("parent_id", id));
		if (count > 0) {
			return Result.fail("请先删除子菜单");
		}

		// 清除所有与该菜单相关的权限缓存
		sysUserService.clearUserAuthorityInfoByMenuId(id);

		sysMenuService.removeById(id);

		// 同步删除中间关联表
		sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id", id));
		return Result.succ("");
	}
}
