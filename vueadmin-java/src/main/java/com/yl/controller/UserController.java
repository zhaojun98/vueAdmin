package com.yl.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.common.dto.PassDto;
import com.yl.common.lang.Const;
import com.yl.common.lang.Result;
import com.yl.common.log.MyLog;
import com.yl.entity.Role;
import com.yl.entity.User;
import com.yl.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户
 * */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/info/{id}")
	@PreAuthorize("hasAuthority('sys:user:list')")
	public Result info(@PathVariable("id") Long id) {

		User sysUser = sysUserService.getById(id);
		Assert.notNull(sysUser, "找不到该管理员");

		List<Role> roles = sysRoleService.listRolesByUserId(id);

		sysUser.setSysRoles(roles);
		return Result.succ(sysUser);
	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('sys:user:list')")
	public Result list(String username) {

		Page<User> pageData = sysUserService.page(getPage(), new QueryWrapper<User>()
				.like(StrUtil.isNotBlank(username), "username", username));

		pageData.getRecords().forEach(u -> {

			u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
		});

		return Result.succ(pageData);
	}

	@PostMapping("/save")
	@PreAuthorize("hasAuthority('sys:user:save')")
	public Result save(@Validated @RequestBody User sysUser) {

		sysUser.setCreateTime(LocalDateTime.now());
		sysUser.setStatus(Const.STATUS_ON);

		// 默认密码
		String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
		sysUser.setPassword(password);

		// 默认头像
		sysUser.setAvatar(Const.DEFULT_AVATAR);

		sysUserService.save(sysUser);
		return Result.succ(sysUser);
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('sys:user:update')")
	public Result update(@Validated @RequestBody User sysUser) {

		sysUser.setUpdateTime(LocalDateTime.now());

		sysUserService.updateById(sysUser);
		return Result.succ(sysUser);
	}

	@Transactional
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('sys:user:delete')")
	public Result delete(@RequestBody Long[] ids) {

		sysUserService.removeByIds(Arrays.asList(ids));
		sysUserRoleService.remove(new QueryWrapper<UserRole>().in("user_id", ids));

		return Result.succ("");
	}

	@Transactional
	@PostMapping("/role/{userId}")
	@PreAuthorize("hasAuthority('sys:user:role')")
	public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {

		List<UserRole> userRoles = new ArrayList<>();

		Arrays.stream(roleIds).forEach(r -> {
			UserRole sysUserRole = new UserRole();
			sysUserRole.setRoleId(r);
			sysUserRole.setUserId(userId);

			userRoles.add(sysUserRole);
		});

		sysUserRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
		sysUserRoleService.saveBatch(userRoles);

		// 删除缓存
		User sysUser = sysUserService.getById(userId);
		sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

		return Result.succ("");
	}

	@MyLog(value = "重置密码")
	@PostMapping("/repass")
	@PreAuthorize("hasAuthority('sys:user:repass')")
	public Result repass(@RequestBody Long userId) {

		User sysUser = sysUserService.getById(userId);

		sysUser.setPassword(passwordEncoder.encode(Const.DEFULT_PASSWORD));
		sysUser.setUpdateTime(LocalDateTime.now());

		sysUserService.updateById(sysUser);
		return Result.succ("");
	}

	@PostMapping("/updatePass")
	public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {

		User sysUser = sysUserService.getByUsername(principal.getName());

		boolean matches = passwordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
		if (!matches) {
			return Result.fail("旧密码不正确");
		}

		sysUser.setPassword(passwordEncoder.encode(passDto.getPassword()));
		sysUser.setUpdateTime(LocalDateTime.now());

		sysUserService.updateById(sysUser);
		return Result.succ("");
	}
}
