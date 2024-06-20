package com.yl.service.impl;

import com.yl.model.entity.UserRole;
import com.yl.mapper.UserRoleMapper;
import com.yl.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
