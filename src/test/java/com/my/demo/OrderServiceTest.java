package com.my.demo;

import com.my.demo.entity.Order;
import com.my.demo.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * @Author Ql
 * @Date 2020/9/17
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    // 测试事务
    @Test
    public void testInserts() {

        Order order = new Order();
        order.setOrderId(1111111);
        order.setUserId(1111111);
        order.setLink("1111111");
        order.setProName("1111111");

        Order order1 = new Order();
        order1.setOrderId(2222);
        order1.setUserId(2222);
        order1.setLink("2222");
        order1.setProName("2222");

        Order order2 = new Order();
        order2.setOrderId(3333333);
        order2.setUserId(3333333);
        order2.setLink("1234567890123");
        order2.setProName("1234567890");

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);
        orders.add(order2);

        orderService.inserts(orders);
    }
}
