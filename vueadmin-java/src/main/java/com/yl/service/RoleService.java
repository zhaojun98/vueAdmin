package com.yl.service;

import com.yl.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
public interface RoleService extends IService<Role> {

	List<Role> listRolesByUserId(Long userId);

}
