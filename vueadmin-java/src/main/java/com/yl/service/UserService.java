package com.yl.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.model.dto.PassDto;
import com.yl.model.entity.User;

import java.security.Principal;

public interface UserService extends IService<User> {

	User getByUsername(String username);

	String getUserAuthorityInfo(Long userId);

	void clearUserAuthorityInfo(String username);

	void clearUserAuthorityInfoByRoleId(Long roleId);

	void clearUserAuthorityInfoByMenuId(Long menuId);


    User info(Long id);

	Page<User> pageList(User user);

	User saveObj(User sysUser);

	User updateObj(User sysUser);

	Boolean delete(Long[] ids);

	Boolean rolePerm(Long userId, Long[] roleIds);

	Boolean repass(Long userId);

	void updatePass(PassDto passDto, Principal principal);
}
