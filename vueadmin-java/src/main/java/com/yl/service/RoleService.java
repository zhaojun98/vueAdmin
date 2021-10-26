package com.yl.service;

import com.yl.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

	List<Role> listRolesByUserId(Long userId);

}
