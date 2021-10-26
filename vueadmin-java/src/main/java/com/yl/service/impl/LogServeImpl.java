package com.yl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.entity.Log;
import com.yl.mapper.LogMapper;
import com.yl.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServeImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
