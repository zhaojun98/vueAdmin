package com.yl.service;

import com.yl.common.dto.SysMenuDto;
import com.yl.entity.Menu;
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
public interface MenuService extends IService<Menu> {

	List<SysMenuDto> getCurrentUserNav();

	List<Menu> tree();

}
