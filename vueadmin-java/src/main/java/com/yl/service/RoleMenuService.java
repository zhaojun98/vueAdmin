package com.yl.service;

import com.yl.model.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RoleMenuService extends IService<RoleMenu> {

    Long[] info(Long roleId,Long[] menuIds);
}
