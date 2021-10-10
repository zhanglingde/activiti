package com.mashibing.activiti.web.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统功能表
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 功能主键
     */
    private Long id;

    /**
     * 父级主键
     */
    private Long parentId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 项目类型:1-菜单2-按钮3-链接4-表单
     */
    private Boolean type;

    /**
     * url地址
     */
    private String url;

    /**
     * 删除标记:0未删除，1删除
     */
    private Boolean isDelete;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUserId;

    /**
     * 修改日期
     */
    private Date updateTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 时间戳
     */
    private Date ts;


}
