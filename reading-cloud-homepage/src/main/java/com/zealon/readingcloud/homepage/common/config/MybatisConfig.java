package com.zealon.readingcloud.homepage.common.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 *MyBatis 配置
 * @author hasee
 */
@Configuration
@MapperScan(basePackages = "com.zealon.readingcloud.homepage.dao",
        sqlSessionTemplateRef = "bookCenterSqlSessionTemplate")
public class MybatisConfig {

    private final static String MAPPER_LOCATIONS = "classpath*:mappers/*.xml";

    /** 工厂配置 */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("bookCenterDataSource") DataSource dataSource) throws Exception {

        //设置数据源
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        //添加xml映射
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factory.setMapperLocations(resolver.getResources(MAPPER_LOCATIONS));


        //添加插件
        factory.setPlugins(new Interceptor[]{ this.getPageHelper() });
        return factory.getObject();
    }

    /** 会话模板 */
    @Bean(name = "bookCenterSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    private PageHelper getPageHelper(){

        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        //分页尺寸为0时查询所有纪录不再执行分页
        properties.setProperty("pageSizeZero", "true");
        //页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("reasonable", "false");
        //支持通过 Mapper 接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        //切换数据源，自动解析不同数据库的分页
        properties.setProperty("autoRuntimeDialect", "true");

        pageHelper.setProperties(properties);
        return pageHelper;

    }

}
