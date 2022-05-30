package com.yl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     */
    @ApiModelProperty("父菜单ID")
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    @ApiModelProperty("菜单URL")
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty("菜单授权码")
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    @ApiModelProperty("组件")
    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @ApiModelProperty("类型 0：目录 1：菜单   2：按钮")
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    @TableField("orderNum")
    private Integer orderNum;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}
