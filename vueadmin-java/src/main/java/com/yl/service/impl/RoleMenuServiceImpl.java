package com.yl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.model.entity.RoleMenu;
import com.yl.mapper.RoleMenuMapper;
import com.yl.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    RoleMenuService roleMenuService;

    @Resource
    UserService userService;

    @Override
    public Long[] info(Long roleId,Long[] menuIds) {


        List<RoleMenu> sysRoleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            sysRoleMenus.add(roleMenu);
        });

        // 先删除原来的记录，再保存新的
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        roleMenuService.saveBatch(sysRoleMenus);

        // 删除缓存
        userService.clearUserAuthorityInfoByRoleId(roleId);
        return menuIds;
    }
}
