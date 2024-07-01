package com.yl.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：jerry
 * @date ：Created in 2024/7/1 09:37
 * @description：
 * @version: V1.1
 */
@Data
public class LoginUserDto {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
