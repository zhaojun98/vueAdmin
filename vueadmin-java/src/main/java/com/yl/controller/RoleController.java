package com.yl.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.common.lang.Const;
import com.yl.common.lang.Result;
import com.yl.entity.Role;
import com.yl.entity.RoleMenu;
import com.yl.entity.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：角色
 * @version: V1.1
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

	@ApiOperation("根据角色id查询菜单")
	@PreAuthorize("hasAuthority('sys:role:list')")
	@GetMapping("/info/{id}")
	public Result info(@PathVariable("id") Long id) {

		Role sysRole = sysRoleService.getById(id);

		// 获取角色相关联的菜单id
		List<RoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
		List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

		sysRole.setMenuIds(menuIds);
		return Result.succ(sysRole);
	}

	@ApiOperation("根据角色名查询")
	@PreAuthorize("hasAuthority('sys:role:list')")
	@GetMapping("/list")
	public Result list(String name) {

		Page<Role> pageData = sysRoleService.page(getPage(),
				new QueryWrapper<Role>()
						.like(StrUtil.isNotBlank(name), "name", name)
		);

		return Result.succ(pageData);
	}

	@ApiOperation("新增")
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('sys:role:save')")
	public Result save(@Validated @RequestBody Role sysRole) {

		sysRole.setCreateTime(LocalDateTime.now());
		sysRole.setStatus(Const.STATUS_ON);

		sysRoleService.save(sysRole);
		return Result.succ(sysRole);
	}

	@ApiOperation("修改")
	@PostMapping("/update")
	@PreAuthorize("hasAuthority('sys:role:update')")
	public Result update(@Validated @RequestBody Role sysRole) {

		sysRole.setUpdateTime(LocalDateTime.now());

		sysRoleService.updateById(sysRole);

		// 更新缓存
		sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());

		return Result.succ(sysRole);
	}

	@ApiOperation("删除")
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('sys:role:delete')")
	@Transactional
	public Result info(@RequestBody Long[] ids) {

		sysRoleService.removeByIds(Arrays.asList(ids));

		// 删除中间表
		sysUserRoleService.remove(new QueryWrapper<UserRole>().in("role_id", ids));
		sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id", ids));

		// 缓存同步删除
		Arrays.stream(ids).forEach(id -> {
			// 更新缓存
			sysUserService.clearUserAuthorityInfoByRoleId(id);
		});

		return Result.succ("");
	}

	@Transactional
	@PostMapping("/perm/{roleId}")
	@PreAuthorize("hasAuthority('sys:role:perm')")
	public Result info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {

		List<RoleMenu> sysRoleMenus = new ArrayList<>();

		Arrays.stream(menuIds).forEach(menuId -> {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setMenuId(menuId);
			roleMenu.setRoleId(roleId);

			sysRoleMenus.add(roleMenu);
		});

		// 先删除原来的记录，再保存新的
		sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
		sysRoleMenuService.saveBatch(sysRoleMenus);

		// 删除缓存
		sysUserService.clearUserAuthorityInfoByRoleId(roleId);

		return Result.succ(menuIds);
	}

}
