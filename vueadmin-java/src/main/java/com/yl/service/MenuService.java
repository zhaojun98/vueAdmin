package com.yl.service;

import cn.hutool.core.lang.tree.Tree;
import com.yl.common.dto.SysMenuDto;
import com.yl.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

	List<SysMenuDto> getCurrentUserNav();

	List<Tree<Long>> tree();

}
