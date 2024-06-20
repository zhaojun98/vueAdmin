package com.yl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.model.entity.Log;

public interface LogService extends IService<Log> {
    Page<Log> pageList(Log log);

}
