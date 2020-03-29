package com.chenyj.moredatasource.datasource.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
@Configuration
@MapperScan(basePackages = {"com.chenyj.moredatasource.datasource.slave.dao"}, sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SalveDataSourceConfig {

    @Bean(name = "slaveDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slavePaginationInterceptor")
    public PaginationInterceptor slavePaginationInterceptor(){
        return new PaginationInterceptor();
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier(value = "slaveDatasource")DataSource dataSource,
                                                    @Qualifier(value = "slavePaginationInterceptor") PaginationInterceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean=new MybatisSqlSessionFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/slave/*.xml");
        bean.setMapperLocations(resources);
        Interceptor[] plugins=new Interceptor[]{paginationInterceptor};
        bean.setPlugins(plugins);
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "slaveDataSourceTransactionManager")
    public DataSourceTransactionManager slaveDataSourceTransactionManager(@Qualifier("slaveDatasource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
