package com.feiniaojin.crepe.boot.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
@Configuration
@ConfigurationProperties(prefix = "h2")
public class DataSourceProperties {

}
