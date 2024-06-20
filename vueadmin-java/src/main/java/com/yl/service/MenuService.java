package com.yl.service;

import cn.hutool.core.lang.tree.Tree;
import com.yl.model.dto.SysMenuDto;
import com.yl.model.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface MenuService extends IService<Menu> {

	List<SysMenuDto> getCurrentUserNav();

	List<Tree<String>> tree();

	Map<Object, Object> nav(Principal principal);


	Menu updateObj(Menu sysMenu);

	Boolean delete(Long id);

	Menu saveObj(Menu sysMenu);
}
