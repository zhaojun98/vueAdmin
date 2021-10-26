package com.yl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.entity.User;

public interface UserService extends IService<User> {

	User getByUsername(String username);

	String getUserAuthorityInfo(Long userId);

	void clearUserAuthorityInfo(String username);

	void clearUserAuthorityInfoByRoleId(Long roleId);

	void clearUserAuthorityInfoByMenuId(Long menuId);


}
