package com.mashibing.activiti.web.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mashibing.activiti.web.module.system.entity.Module;
import com.mashibing.activiti.web.module.system.entity.User;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 孙志强
 * @since 2020-04-13
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> getRoleCodeList(Long userId);
    List<String> queryAllPerms(Long userId);
    List<Module> findMenuListByUserId(Long userId);
}
