package com.yl.service;

import com.yl.common.dto.SysMenuDto;
import com.yl.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

	List<SysMenuDto> getCurrentUserNav();

	List<Menu> tree();

}
