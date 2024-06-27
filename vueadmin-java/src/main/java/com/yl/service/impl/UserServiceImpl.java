package com.yl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.constant.Const;
import com.yl.model.dto.PassDto;
import com.yl.common.exception.CustomException;
import com.yl.constant.RedisKeyConstant;
import com.yl.mapper.UserMapper;
import com.yl.model.entity.Menu;
import com.yl.model.entity.Role;
import com.yl.model.entity.User;
import com.yl.model.entity.UserRole;
import com.yl.security.MyPasswordEncoder;
import com.yl.service.MenuService;
import com.yl.service.RoleService;
import com.yl.service.UserRoleService;
import com.yl.service.UserService;
import com.yl.utils.RedisTools;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Resource
	RoleService sysRoleService;

	@Resource
	UserMapper sysUserMapper;

	@Resource
	MenuService sysMenuService;

	@Resource
	RedisTools redisTools;

	@Resource
	UserRoleService userRoleService;

	@Resource
	MyPasswordEncoder passwordEncoder;


	@Override
	public User getByUsername(String username) {
		return getOne(new QueryWrapper<User>().eq("username", username));
	}

	@Override
	public String getUserAuthorityInfo(Long userId) {

		User sysUser = sysUserMapper.selectById(userId);

		String authority = "";

		if (redisTools.hasKey(RedisKeyConstant.GrantedAuthority + sysUser.getUsername())) {
			authority = (String) redisTools.get(RedisKeyConstant.GrantedAuthority + sysUser.getUsername());

		} else {
			// 获取角色编码
			List<Role> roles = sysRoleService.list(new QueryWrapper<Role>()
					.inSql("id", "select role_id from sys_user_role where user_id = " + userId));

			if (roles.size() > 0) {
				String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
				authority = roleCodes.concat(",");
			}

			// 获取菜单操作编码
			List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
			if (menuIds.size() > 0) {

				List<Menu> menus = sysMenuService.listByIds(menuIds);
				String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));

				authority = authority.concat(menuPerms);
			}
			redisTools.set(RedisKeyConstant.GrantedAuthority + sysUser.getUsername(), authority, 60 * 60);
		}

		return authority;
	}

	@Override
	public void clearUserAuthorityInfo(String username) {
		redisTools.del(RedisKeyConstant.GrantedAuthority+ username);
	}

	@Override
	public void clearUserAuthorityInfoByRoleId(Long roleId) {

		List<User> sysUsers = this.list(new QueryWrapper<User>()
				.inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

		sysUsers.forEach(u -> {
			this.clearUserAuthorityInfo(u.getUsername());
		});

	}

	@Override
	public void clearUserAuthorityInfoByMenuId(Long menuId) {
		List<User> sysUsers = sysUserMapper.listByMenuId(menuId);

		sysUsers.forEach(u -> {
			this.clearUserAuthorityInfo(u.getUsername());
		});
	}

	@Override
	public User info(Long id) {
		User sysUser = this.getById(id);
		Assert.notNull(sysUser, "找不到该管理员");
		List<Role> roles = sysRoleService.listRolesByUserId(id);
		sysUser.setSysRoles(roles);
		return sysUser;
	}

	@Override
	public Page<User> pageList(User user) {

		Page<User> pageData = this.page(new Page<>(user.getCurrent(),user.getSize()),
				new QueryWrapper<User>()
						.like(StrUtil.isNotBlank(user.getUsername()), "username", user.getUsername()));

		pageData.getRecords().forEach(u -> {

			u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
		});
		return pageData;
	}

	@Override
	public User saveObj(User sysUser) {
		sysUser.setCreateTime(LocalDateTime.now());
		sysUser.setStatus(Const.STATUS_ON);
		// 默认密码
		String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
		sysUser.setPassword(password);
		// 默认头像
		sysUser.setAvatar(Const.DEFULT_AVATAR);
		boolean save = this.save(sysUser);
		if (!save) throw new CustomException("保存数据失败");
		return sysUser;
	}

	@Override
	public User updateObj(User sysUser) {
		sysUser.setUpdateTime(LocalDateTime.now());
		boolean b = this.updateById(sysUser);
		if (!b) throw new CustomException("更新数据失败");
		return sysUser;
	}

	@Override
	public Boolean delete(Long[] ids) {
		boolean b = this.removeByIds(Arrays.asList(ids));
		if (!b) throw new CustomException("删除数据失败");
		boolean remove = userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", ids));
		if (!remove) throw new CustomException("删除数据失败");
		return true;
	}

	@Override
	public Boolean rolePerm(Long userId, Long[] roleIds) {

		List<UserRole> userRoles = new ArrayList<>();

		Arrays.stream(roleIds).forEach(r -> {
			UserRole sysUserRole = new UserRole();
			sysUserRole.setRoleId(r);
			sysUserRole.setUserId(userId);

			userRoles.add(sysUserRole);
		});

		userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
		userRoleService.saveBatch(userRoles);

		// 删除缓存
		User sysUser = this.getById(userId);
		this.clearUserAuthorityInfo(sysUser.getUsername());
		return true;
	}

	@Override
	public Boolean repass(Long userId) {

		User sysUser = this.getById(userId);

		sysUser.setPassword(passwordEncoder.encode(Const.DEFULT_PASSWORD));
		sysUser.setUpdateTime(LocalDateTime.now());

		boolean b = this.updateById(sysUser);
		if(!b) throw new CustomException("修改失败");

		return true;
	}

	@Override
	public void updatePass(PassDto passDto, Principal principal) {
		User sysUser = this.getByUsername(principal.getName());

		boolean matches = passwordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
		if (!matches) {
			throw new CustomException("旧密码不正确");
		}

		sysUser.setPassword(passwordEncoder.encode(passDto.getPassword()));
		sysUser.setUpdateTime(LocalDateTime.now());

		boolean b = this.updateById(sysUser);
		if(!b) throw new CustomException("修改失败");
	}
}
