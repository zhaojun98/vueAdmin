package com.yl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.model.entity.Log;
import com.yl.mapper.LogMapper;
import com.yl.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServeImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public Page<Log> pageList(Log log) {
        return this.page(new Page<>(log.getCurrent(), log.getSize()), new QueryWrapper<Log>()
                .orderByAsc("create_time"));
    }
}
