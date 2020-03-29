package com.chenyj.moredatasource.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
@Configuration
@MapperScan(basePackages = {"com.chenyj.moredatasource.datasource.master.dao"}, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    @Bean(name = "masterDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterPaginationInterceptor")
    public PaginationInterceptor masterPaginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier(value = "masterDatasource") DataSource dataSource,
                                                     @Qualifier(value = "masterPaginationInterceptor") PaginationInterceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/master/*.xml");
        bean.setMapperLocations(resources);
        Interceptor[] plugins = new Interceptor[]{paginationInterceptor};
        bean.setPlugins(plugins);
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "masterDataSourceTransactionManager")
    public DataSourceTransactionManager masterDataSourceTransactionManager(@Qualifier("masterDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
