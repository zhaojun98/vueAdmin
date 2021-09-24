package com.yl.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.common.dto.SysMenuDto;
import com.yl.entity.Menu;

import com.yl.entity.User;
import com.yl.mapper.MenuMapper;
import com.yl.mapper.UserMapper;
import com.yl.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-09-023
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

	@Autowired
	UserService sysUserService;

	@Autowired
	UserMapper sysUserMapper;

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
	public List<Menu> tree() {
		// 获取所有菜单信息
		List<Menu> sysMenus = this.list(new QueryWrapper<Menu>().orderByAsc("orderNum"));

		// 转成树状结构
		return buildTreeMenu(sysMenus);
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

//		System.out.println(JSONUtil.toJsonStr(finalMenus));
		return finalMenus;
	}
}
