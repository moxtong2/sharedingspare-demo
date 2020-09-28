package com.my.demo.mapper;

import com.my.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    int count();

    List<Order> findAll(Integer page, Integer pageSize);

    int insert(Order order);

    List<Order> selectJoin();
}
