package com.microservice.product.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value="file:./application-local.properties",ignoreResourceNotFound=true)
public class HibernateConfig {
    @Autowired private Environment env;
    @Bean public DataSource dataSource() {
        DriverManagerDataSource ds=new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("jdbc.driver"));
        ds.setUrl(env.getRequiredProperty("jdbc.url"));
        ds.setUsername(env.getRequiredProperty("jdbc.username"));
        ds.setPassword(env.getRequiredProperty("jdbc.password"));
        return ds;
    }
    @Bean public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory=new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("com.microservice.product.entity");
        Properties p=new Properties();
        p.put("hibernate.dialect",env.getRequiredProperty("hibernate.dialect"));
        p.put("hibernate.show_sql",env.getRequiredProperty("hibernate.show_sql"));
        p.put("hibernate.hbm2ddl.auto",env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        p.put("hibernate.format_sql","true");
        factory.setHibernateProperties(p);
        return factory;
    }
}
