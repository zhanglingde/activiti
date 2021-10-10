package com.mashibing.activiti.web.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键
     */
    private Long id;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 删除标记:0未删除，1删除
     */
    private Boolean isDelete;

    /**
     * 备注
     */
    private String mark;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUser;

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
