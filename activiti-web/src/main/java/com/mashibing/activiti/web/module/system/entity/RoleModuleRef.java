package com.mashibing.activiti.web.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色功能关联表
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_module_ref")
public class RoleModuleRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户角色关联
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long moduleId;

    /**
     * 角色主键
     */
    private Long roleId;


}
