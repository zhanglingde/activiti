package com.mashibing.activiti.web.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录账户
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 盐值，密码秘钥
     */
    private String secretkey;

    private Integer locked;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 删除标记:0未删除，1删除
     */
    private Integer isDelete;

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
    private Date updateUser;

    /**
     * 修改用户
     */
    private Date updateTime;

    /**
     * 时间戳
     */
    private Date ts;


}
