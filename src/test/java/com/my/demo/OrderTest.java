package com.my.demo;

import com.alibaba.fastjson.JSON;
import com.my.demo.entity.Order;
import com.my.demo.entity.OrderItem;
import com.my.demo.mapper.OrderItemMapper;
import com.my.demo.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Ql
 * @Date 2020/9/16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;


    @Test
    public void testSelectAll () {
        // 单表查询
        List<Order> all = orderMapper.findAll(null, null);
        System.err.println(all.size());

        all.forEach(e -> {
            System.err.println(JSON.toJSONString(e));
        });
    }

    @Test
    public void testSelectPage () {
        // 单表分页查询查询
        int count = orderMapper.count();
        int pageSize = 2;
        int pageCount = (count + pageSize -1) / pageSize;

        ArrayList<Order> all = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            int page = i * pageSize;
            List<Order> list = orderMapper.findAll(page, pageSize);
            System.err.println(list.size());
            all.addAll(list);
        }
        all.forEach(e -> {
            System.err.println(JSON.toJSONString(e));
        });
    }


    @Test
    public void testSelectJoin () {
        // 连表查询
        List<Order> all = orderMapper.selectJoin();
        System.err.println(all.size());

        all.forEach(e -> {
            System.err.println(JSON.toJSONString(e));
        });
    }

    @Test
    public void testInsertJoin () {
        // 两关联表插入
         for (int i = 0; i < 20; i++) {
             int i1 = new Random().nextInt(50000);
             int i2 = new Random().nextInt(50000);
             Order order = new Order();
             order.setOrderId(i1);
             order.setUserId(i2);
             order.setLink("546546");
             order.setProName("dsfdsf");

             OrderItem orderItem = new OrderItem();
             orderItem.setOrderId(i1);
             orderItem.setUserId(i2);
             orderItem.setLink("546546");
             orderItem.setProName("dsfdsf");

             orderMapper.insert(order);
             orderItemMapper.insert(orderItem);
         }
    }

    @Test
    public void testInsert () {
        // 单表插入
         for (int i = 0; i < 20; i++) {
             int i1 = new Random().nextInt(50000);
             int i2 = new Random().nextInt(50000);
             Order order = new Order();
             order.setOrderId(i1);
             order.setUserId(i2);
             order.setLink("546546");
             order.setProName("dsfdsf");
             orderMapper.insert(order);
         }
    }

    // 创建表
    @Test
    public void testCreateTable() {

        orderItemMapper.createOrderTable();

        orderItemMapper.createOrderItemsTable();
    }




}



























