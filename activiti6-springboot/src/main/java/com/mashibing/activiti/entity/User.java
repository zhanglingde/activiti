package com.mashibing.activiti.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 孙志强
 * @date 2019/9/10 11:58
 */
@Data
@Accessors(chain = true)
public class User  implements Serializable {
    //姓名
    private String name;
    //用户级别
    private int level;
}