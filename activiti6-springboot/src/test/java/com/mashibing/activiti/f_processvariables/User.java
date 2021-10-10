package com.mashibing.activiti.f_processvariables;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-08-08 21:57
 */
@AllArgsConstructor
@Data
public class User implements Serializable {

    private String userId;
    private String username;
}
