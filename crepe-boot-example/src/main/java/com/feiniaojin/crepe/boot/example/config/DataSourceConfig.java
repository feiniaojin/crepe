package com.feiniaojin.crepe.boot.example.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据源配置
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSourceDb0")
    @Primary
    @ConfigurationProperties(prefix = "hikari.db0")
    public HikariDataSource dataSourceDb0() {
        return new HikariDataSource();
    }

    @Bean(name = "dataSourceDb1")
    @ConfigurationProperties(prefix = "hikari.db1")
    public HikariDataSource dataSourceDb1() {
        return new HikariDataSource();
    }

    @Bean(name = "dataSourceDb2")
    @ConfigurationProperties(prefix = "hikari.db2")
    public HikariDataSource dataSourceDb2() {
        return new HikariDataSource();
    }

    @Bean(name = "dataSourceDb3")
    @ConfigurationProperties(prefix = "hikari.db3")
    public HikariDataSource dataSourceDb3() {
        return new HikariDataSource();
    }

    @Bean(name = "dataSourceDb4")
    @ConfigurationProperties(prefix = "hikari.db4")
    public HikariDataSource dataSourceDb4() {
        return new HikariDataSource();
    }

    @Bean(name = "dataSourceDb5")
    @ConfigurationProperties(prefix = "hikari.db5")
    public HikariDataSource dataSourceDb5() {
        return new HikariDataSource();
    }

}
