package com.yl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

	List<Role> listRolesByUserId(Long userId);

    Role info(Long id);

    Page<Role> pageList(Role role);

    Role saveObj(Role sysRole);

    Role updateObj(Role sysRole);

    void delete(Long[] ids);

}
