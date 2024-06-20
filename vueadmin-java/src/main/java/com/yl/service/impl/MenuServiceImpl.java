package com.yl.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.common.CommonResultVo;
import com.yl.common.exception.CustomException;
import com.yl.model.dto.SysMenuDto;
import com.yl.model.entity.Menu;

import com.yl.model.entity.RoleMenu;
import com.yl.model.entity.User;
import com.yl.mapper.MenuMapper;
import com.yl.mapper.UserMapper;
import com.yl.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.service.RoleMenuService;
import com.yl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    UserService sysUserService;

    @Resource
    UserMapper sysUserMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleMenuService roleMenuService;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sysUser = sysUserService.getByUsername(username);

        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<Menu> menus = this.listByIds(menuIds);

        // 转树状结构
        List<Menu> menuTree = buildTreeMenu(menus);

        // 实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<Tree<String>> tree() {

        //方式一
        // 获取所有菜单信息
//        List<Menu> sysMenus = this.list(new QueryWrapper<Menu>().orderByAsc("orderNum"));
        // 转成树状结构
        //		return buildTreeMenu(sysMenus);


        //方式二
        //利用mysql
        List<Menu> treeList = menuMapper.tree();        //mysql递归查询语法
        //todo ID转Strung,应为Long过长类型传前端精度丢失
        //用hutool工具包转换成数据结构的新方式
        List<Tree<String>> build = TreeUtil.build(treeList, "0", new TreeNodeConfig(),
                (menu, treeNode) -> {
                    treeNode.setId(menu.getId() + "");
                    treeNode.setParentId(menu.getParentId() + "");
                    treeNode.setName(String.valueOf(menu.getName()));
                    treeNode.put("path", menu.getPath());
                    treeNode.put("status", menu.getStatus());
                    treeNode.put("icon", menu.getIcon());
                    treeNode.put("component", menu.getComponent());
                    treeNode.put("perms", menu.getPerms());
                    treeNode.put("type", menu.getType());
                    treeNode.put("orderNum", menu.getOrderNum());
                    treeNode.put("children", menu.getChildren());
                });


        return build;

    }


    private List<SysMenuDto> convert(List<Menu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();

            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<Menu> buildTreeMenu(List<Menu> menus) {

        List<Menu> finalMenus = new ArrayList<>();
        // 先各自寻找到各自的孩子
        for (Menu menu : menus) {
            for (Menu e : menus) {
                if (menu.getId() == e.getParentId()) {
                    menu.getChildren().add(e);
                }
            }
            // 提取出父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }

    @Override
    public Map<Object, Object> nav(Principal principal) {

        User sysUser = sysUserService.getByUsername(principal.getName());

        // 获取权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());// ROLE_admin,ROLE_normal,sys:user:list,....
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        // 获取导航栏信息
        List<SysMenuDto> navs = this.getCurrentUserNav();

        return MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map();


    }

    @Override
    public Menu updateObj(Menu sysMenu) {
        sysMenu.setUpdateTime(LocalDateTime.now());

        boolean b = this.updateById(sysMenu);
        if (!b) throw new CustomException("更新数据成功");

        // 清除所有与该菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        return sysMenu;
    }

    @Override
    public Boolean delete(Long id) {

        int count = this.count(new QueryWrapper<Menu>().eq("parent_id", id));
        if (count > 0) {
            throw new CustomException("请先删除子菜单");
        }

        // 清除所有与该菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(id);

        boolean b = this.removeById(id);
        if (!b) throw new CustomException("删除数据失败");
        // 同步删除中间关联表
        boolean remove = roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id", id));
        if (!remove) throw new CustomException("删除数据失败");
        return true;
    }

    @Override
    public Menu saveObj(Menu sysMenu) {
        sysMenu.setCreateTime(LocalDateTime.now());
        boolean save = this.save(sysMenu);
        if(!save) throw new CustomException("新增数据失败");
        return sysMenu;
    }
}
