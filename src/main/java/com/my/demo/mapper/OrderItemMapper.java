package com.my.demo.mapper;

import com.my.demo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Ql
 * @Date 2020/9/16
 **/
@Mapper
public interface OrderItemMapper {

    int createOrderTable();

    int createOrderItemsTable();

    int insert(OrderItem orderItem);

}
