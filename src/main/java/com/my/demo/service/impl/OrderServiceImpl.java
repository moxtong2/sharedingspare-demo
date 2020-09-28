package com.my.demo.service.impl;

import com.my.demo.entity.Order;
import com.my.demo.mapper.OrderMapper;
import com.my.demo.service.OrderService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Ql
 * @Date 2020/9/17
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void inserts(List<Order> orders) {

        for (int i = 0; i < orders.size(); i++) {
            orderMapper.insert(orders.get(i));
            System.err.println("insert" + i);
        }

    }
}
