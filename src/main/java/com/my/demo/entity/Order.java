package com.my.demo.entity;

import lombok.Data;

/**
 * @Author Ql
 * @Date 2020/9/16
 **/
@Data
public class Order {

    private Integer orderId;
    private Integer userId;
    private String proName;
    private String link;
}
