package com.my.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Author Ql
 * @Date 2020/9/15
 **/
@SpringBootApplication
public class App {

    public static void main(String[] args) {


        SpringApplication.run(App.class, args);
    }

}
