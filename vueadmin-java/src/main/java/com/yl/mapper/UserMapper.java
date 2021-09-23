package com.yl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yl.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

	List<Long> getNavMenuIds(Long userId);

	List<User> listByMenuId(Long menuId);
}
