package com.github.diegopacheco.sandboxspring.infra;

import com.github.diegopacheco.sandboxspring.conf.DBConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

@Component
public class ConnectionRefresher implements ApplicationContextAware {

    ApplicationContext ctx;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private HikariDataSource ds;

    @Autowired
    private DBConfiguration configuration;

    private boolean done = false;

    @Scheduled(fixedRate = 1000)
    public void refresh() throws Exception {
        if (!done){
            System.out.println("Refreshing connection... TX manager: " + txManager + " - DS: " + ds);
            for(int i=0;i<10;i++) {
               System.out.println(ds.getConnection());
            }

            ds.getHikariPoolMXBean().softEvictConnections();
            done = true;
            System.out.println("Connection soft evicted!");

            /*
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory)ctx.getAutowireCapableBeanFactory();
            DefaultListableBeanFactory defaultListableBeanFactory = bf;
            defaultListableBeanFactory.removeBeanDefinition("transactionManager");
            defaultListableBeanFactory.registerSingleton("transactionManager", configuration.txManager()); // No bean named 'transactionManager' available
            txManager = (PlatformTransactionManager) defaultListableBeanFactory.getBean("transactionManager");
            */

            txManager = (PlatformTransactionManager) ctx.getBean("transactionManager");
            ds = (HikariDataSource) ctx.getBean("dataSource");
            System.out.println("Connection refreshed!");
            for(int i=0;i<10;i++) {
                System.out.println(ds.getConnection());
            }
        }
        System.out.println("*** TX manager: " + txManager + " - DS: " + ds);
        System.out.println(" Active connections : " + ds.getHikariPoolMXBean().getActiveConnections());
        System.out.println(" Idle connections   : " + ds.getHikariPoolMXBean().getIdleConnections());
        System.out.println(" Total connections  : " + ds.getHikariPoolMXBean().getTotalConnections());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}