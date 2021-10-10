package com.mashibing.activiti.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @author 孙志强
 * @date 2019/9/10 11:58
 */
@Data
@Accessors(chain = true)
public class Order implements Serializable {
    //下单日期
    private Date bookingDate;
    //订单原价金额
    private int amout;
    //下单人
    private User user;
    //积分
    private int score;
}