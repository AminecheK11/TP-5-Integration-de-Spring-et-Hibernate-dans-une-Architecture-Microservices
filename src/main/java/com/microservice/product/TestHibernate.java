package com.microservice.product;
import com.microservice.product.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.sql.DataSource;
public class TestHibernate {
    public static void main(String[] args) throws Exception {
        try(AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class);
            var connection=context.getBean(DataSource.class).getConnection();
            var statement=connection.createStatement();
            var result=statement.executeQuery("SELECT DATABASE(), VERSION()")) {
            result.next();
            System.out.println("Contexte Spring initialise avec succes !");
            System.out.println("Connexion MySQL : "+result.getString(1)+" / "+result.getString(2));
        }
    }
}
