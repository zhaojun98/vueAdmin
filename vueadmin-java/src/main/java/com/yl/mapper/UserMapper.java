package com.yl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yl.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：角色菜单mapper
 * @version: V1.1
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

	List<Long> getNavMenuIds(Long userId);

	List<User> listByMenuId(Long menuId);
}
