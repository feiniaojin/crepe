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
}
