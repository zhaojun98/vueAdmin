package com.yl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.service.*;
import com.yl.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	@Autowired
	HttpServletRequest req;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	UserService sysUserService;

	@Autowired
	RoleService sysRoleService;

	@Autowired
    MenuService sysMenuService;

	@Autowired
	UserRoleService sysUserRoleService;

	@Autowired
	RoleMenuService sysRoleMenuService;

	/**
	 * 获取页面
	 * @return
	 */
	public Page getPage() {
		int current = ServletRequestUtils.getIntParameter(req, "cuurent", 1);
		int size = ServletRequestUtils.getIntParameter(req, "size", 10);

		return new Page(current, size);
	}

}
