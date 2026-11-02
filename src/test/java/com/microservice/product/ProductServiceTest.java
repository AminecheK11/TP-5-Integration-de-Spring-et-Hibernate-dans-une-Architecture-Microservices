package com.microservice.product;
import com.microservice.product.config.AppConfig;
import com.microservice.product.dao.ProductDaoImpl;
import com.microservice.product.entity.Product;
import com.microservice.product.service.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    static AnnotationConfigApplicationContext context;
    static ProductService service;
    static SessionFactory factory;
    static TransactionTemplate tx;
    @BeforeAll static void init() {
        context=new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("isolated-tests",Map.of(
            "jdbc.url","jdbc:mysql://localhost:3306/product_db_test?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            "hibernate.hbm2ddl.auto","create-drop","hibernate.show_sql","false")));
        context.register(AppConfig.class); context.refresh();
        service=context.getBean(ProductService.class); factory=context.getBean(SessionFactory.class);
        tx=new TransactionTemplate(context.getBean(PlatformTransactionManager.class));
    }
    @AfterAll static void close() { if(context!=null) context.close(); }
    @BeforeEach void clean() {
        tx.execute(status -> {
            factory.getCurrentSession().doWork(c -> {
                if(!"product_db_test".equals(c.getCatalog())) throw new IllegalStateException("Base de test incorrecte");
            });
            factory.getCurrentSession().createQuery("delete from Product").executeUpdate(); return null;
        });
    }
    Product create() { Product p=new Product("Clavier",25); service.addProduct(p); return p; }
    @Test void contexteEtProxySpring() {
        assertTrue(AopUtils.isAopProxy(service));
        assertSame(service,context.getBean(ProductServiceImpl.class));
        assertNotNull(context.getBean(ProductDaoImpl.class));
    }
    @Test void creationEtLecture() {
        Product p=create(); assertNotNull(p.getId());
        Product lu=service.getProductById(p.getId()); assertEquals("Clavier",lu.getName()); assertEquals(25,lu.getPrice());
    }
    @Test void miseAJour() {
        Product p=create(); p.setName("Souris"); p.setPrice(35); service.updateProduct(p);
        assertEquals("Souris",service.getProductById(p.getId()).getName()); assertEquals(35,service.getProductById(p.getId()).getPrice());
    }
    @Test void suppression() { Product p=create(); service.deleteProduct(p); assertNull(service.getProductById(p.getId())); }
    @Test void liste() { create(); create(); assertEquals(2,service.getAllProducts().size()); }
    @Test void absent() { assertNull(service.getProductById(-1L)); }
    @Test void prixNegatif() {
        assertThrows(IllegalArgumentException.class,()->service.addProduct(new Product("Erreur",-1))); assertTrue(service.getAllProducts().isEmpty());
    }
    @Test void prixZero() { Product p=new Product("Gratuit",0); service.addProduct(p); assertNotNull(p.getId()); }
    @Test void prixNonFini() {
        assertThrows(IllegalArgumentException.class,()->service.addProduct(new Product("NaN",Double.NaN)));
        assertThrows(IllegalArgumentException.class,()->service.addProduct(new Product("Infini",Double.POSITIVE_INFINITY)));
    }
    @Test void nomObligatoire() {
        assertThrows(IllegalArgumentException.class,()->service.addProduct(new Product(null,1)));
        assertThrows(IllegalArgumentException.class,()->service.addProduct(new Product(" ",1)));
    }
    @Test void lotValide() {
        service.addProducts(List.of(new Product("A",10),new Product("B",20))); assertEquals(2,service.getAllProducts().size());
    }
    @Test void rollbackMetierApresPremiereInsertion() {
        Product existant=create(),premier=new Product("A",10);
        assertThrows(IllegalArgumentException.class,()->service.addProducts(List.of(premier,new Product("B",-1))));
        assertNotNull(premier.getId(),"IDENTITY prouve que le premier INSERT avait eu lieu");
        assertNull(service.getProductById(premier.getId()));
        assertEquals(List.of(existant.getId()),service.getAllProducts().stream().map(Product::getId).toList());
        create(); assertEquals(2,service.getAllProducts().size());
    }
    @Test void rollbackErreurSql() {
        Product premier=new Product("A",10);
        assertThrows(RuntimeException.class,()->service.addProducts(List.of(premier,new Product("X".repeat(300),20))));
        assertNotNull(premier.getId()); assertTrue(service.getAllProducts().isEmpty());
    }
    @Test void miseAJourInvalideConserveEtat() {
        Product p=create(); p.setPrice(-1); assertThrows(IllegalArgumentException.class,()->service.updateProduct(p));
        assertEquals(25,service.getProductById(p.getId()).getPrice());
    }
    @Test void daoExigeTransactionSpring() {
        assertThrows(org.hibernate.HibernateException.class,()->context.getBean(ProductDaoImpl.class).findAll());
    }
    @Test void contrainteNomEnBase() {
        assertThrows(RuntimeException.class,()->tx.execute(status -> {
            context.getBean(ProductDaoImpl.class).save(new Product(null,10)); return null;
        }));
        assertTrue(service.getAllProducts().isEmpty());
    }
}
