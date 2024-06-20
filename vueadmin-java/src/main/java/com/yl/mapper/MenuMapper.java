package com.yl.mapper;

import com.yl.model.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：菜单mapper
 * @version: V1.1
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> tree();

}
