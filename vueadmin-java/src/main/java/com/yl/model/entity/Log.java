package com.yl.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("sys_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class Log extends BaseEntity {

    @ApiModelProperty("用户名")
    private String username; //用户名

    @ApiModelProperty("操作")
    private String operation; //操作

    @ApiModelProperty("方法名")
    private String method; //方法名

    @ApiModelProperty("参数")
    private String params; //参数

    @ApiModelProperty("ip地址")
    private String ip; //ip地址

}