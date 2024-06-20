package com.yl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.asept.DeptAspect;
import com.yl.common.exception.CustomException;
import com.yl.constant.Const;
import com.yl.mapper.RoleMapper;
import com.yl.model.entity.Role;
import com.yl.model.entity.RoleMenu;
import com.yl.model.entity.UserRole;
import com.yl.service.RoleMenuService;
import com.yl.service.RoleService;
import com.yl.service.UserRoleService;
import com.yl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Resource
	RoleMenuService roleMenuService;

	@Resource
	DeptAspect deptAspect;


	@Autowired
	RoleService sysRoleService;

	@Autowired
	UserService sysUserService;

	@Autowired
	RoleMenuService sysRoleMenuService;

	@Autowired
	UserRoleService sysUserRoleService;

	@Override
	public List<Role> listRolesByUserId(Long userId) {

		List<Role> sysRoles = this.list(new QueryWrapper<Role>()
				.inSql("id", "select role_id from sys_user_role where user_id = " + userId));

		return sysRoles;
	}

	@Override
	public Role info(Long id) {

		Role sysRole = this.getById(id);

		// 获取角色相关联的菜单id
		List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
		List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

		sysRole.setMenuIds(menuIds);
		return sysRole;
	}

	@Override
	public Page<Role> pageList(Role role) {
		List<String> deptList = deptAspect.getDeptList();        //权限过滤接口

		return sysRoleService.page(new Page<>(role.getCurrent(), role.getSize()),
				new QueryWrapper<Role>()
						.like(StrUtil.isNotBlank(role.getName()), "name", role.getName()));

	}

	@Override
	public Role saveObj(Role sysRole) {
		sysRole.setCreateTime(LocalDateTime.now());
		sysRole.setStatus(Const.STATUS_ON);
		sysRoleService.save(sysRole);
		return sysRole;
	}

	@Override
	public Role updateObj(Role sysRole) {
		sysRole.setUpdateTime(LocalDateTime.now());
		boolean b = sysRoleService.updateById(sysRole);
		if(!b) throw new CustomException("更新数据失败");
		// 更新缓存
		sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
		return sysRole;
	}

	@Override
	public void delete(Long[] ids) {
		boolean b = sysRoleService.removeByIds(Arrays.asList(ids));
		if(!b) throw new CustomException("删除数据失败");
		// 删除中间表
		boolean remove = sysUserRoleService.remove(new QueryWrapper<UserRole>().in("role_id", ids));
		if(!remove) throw new CustomException("删除数据失败");
		boolean remove1 = sysRoleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id", ids));
		if(!remove1) throw new CustomException("删除数据失败");
		// 缓存同步删除
		Arrays.stream(ids).forEach(id -> {
			// 更新缓存
			sysUserService.clearUserAuthorityInfoByRoleId(id);
		});
	}
}
