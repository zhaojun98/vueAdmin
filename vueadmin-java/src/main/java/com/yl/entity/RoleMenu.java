package com.yl.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：菜单角色
 * @version: V1.1
 */
@Data
public class RoleMenu {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roleId;

    private Long menuId;


}
