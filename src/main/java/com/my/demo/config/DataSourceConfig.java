package com.my.demo.config;

import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Ql
 * @Date 2020/9/16
 **/
@Configuration
public class DataSourceConfig {

    @Autowired
    private CommonsLogFilter slf4jLogFilter;

    @Bean(name = "shardingDataSource")
    @Qualifier("shardingDataSource")
    public DataSource getShardingDataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        DruidDataSource dataSource1 = createDefaultDruidDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://10.20.*.*:3306/sp");
        dataSource1.setUsername("root");
        dataSource1.setPassword("*");
        dataSource1.setFilters("wall,stat,slf4j,commonLogging");
        dataSource1.getProxyFilters().add(slf4jLogFilter);
        dataSourceMap.put("ds0", dataSource1);

        // 配置第二个数据源
        DruidDataSource dataSource2 = createDefaultDruidDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://10.20.*.*:3306/sp");
        dataSource2.setUsername("root");
        dataSource2.setPassword("*");
        dataSource2.getProxyFilters().add(slf4jLogFilter);
        dataSource2.setFilters("wall,stat,slf4j,commonLogging");
        dataSourceMap.put("ds1", dataSource2);

        //1、第一个表 t_order 则配置
        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        // 逻辑表 （xml 写 sql 用此表名）
        orderTableRuleConfig.setLogicTable("t_order");
        // 实际表
        orderTableRuleConfig.setActualDataNodes("ds${0..1}.t_order_${0..1}");

        // 配置分库策略（Groovy表达式配置db规则）
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));

        // 配置分表策略（Groovy表达式配置表路由规则）
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2}"));


        // 序列id 字段名 （常用数据库主键生成， 可选雪花算法）
//        orderTableRuleConfig.setKeyGeneratorColumnName("xxx");

        // 上面 序列id 生成器
//        orderTableRuleConfig.setKeyGenerator(() -> 1);


        //===========================================
        //2、第一个表 t_order_items 则配置
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
        orderItemTableRuleConfig.setLogicTable("t_order_items");
        orderItemTableRuleConfig.setActualDataNodes("ds${0..1}.t_order_items_${0..1}");
        orderItemTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        orderItemTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_items_${order_id % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        Collection<TableRuleConfiguration> tableRuleConfigs = shardingRuleConfig.getTableRuleConfigs();
        tableRuleConfigs.add(orderItemTableRuleConfig);
        tableRuleConfigs.add(orderTableRuleConfig);

        // 绑定表配置， 只有数据库分片规则相同且分表规则相同 可以配置绑定表关系提高性能
        shardingRuleConfig.setBindingTableGroups(Arrays.asList("t_order, t_order_items"));

        // 广播表配置， 类似字典表可以配置广播，即每个库里都有相同的表及数据
        shardingRuleConfig.setBroadcastTables(Arrays.asList("order_dict"));


        // 获取数据源对象
        DataSource dataSource = null;
        try {
            Properties props = new Properties();
            props.put("sql.show", "true");
//            props.put("sql.simple", "true");
            dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }


    private DruidDataSource createDefaultDruidDataSource() {
        return new DruidDataSource();
    }

    // 可配置慢SQL打印
    @Bean
    public CommonsLogFilter slf4jLogFilter() {
//        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        CommonsLogFilter slf4jLogFilter = new CommonsLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(true);
        slf4jLogFilter.setConnectionLogEnabled(true);
        slf4jLogFilter.setStatementLogEnabled(true);
        slf4jLogFilter.setResultSetLogEnabled(true);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }
}
