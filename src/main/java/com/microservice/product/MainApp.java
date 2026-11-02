package com.microservice.product;
import com.microservice.product.config.AppConfig;
import com.microservice.product.entity.Product;
import com.microservice.product.service.ProductServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
public class MainApp {
    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class)) {
            ProductServiceImpl service=context.getBean(ProductServiceImpl.class);
            Product p=new Product("Microservice Gateway",149.99);
            service.addProduct(p);
            System.out.println("Produit ajoute : id="+p.getId()+", prix="+p.getPrice());
            System.out.println("Produit ajoute avec succes ! Total en base : "+service.getAllProducts().size());
            long avant=service.getAllProducts().size();
            try {
                service.addProducts(List.of(new Product("Insertion a annuler",10),new Product("Prix invalide",-1)));
                throw new IllegalStateException("Le lot aurait du echouer.");
            } catch(IllegalArgumentException attendu) {
                long apres=service.getAllProducts().size();
                if(avant!=apres) throw new IllegalStateException("Rollback non effectue.");
                System.out.println("Rollback Spring confirme : total avant="+avant+", apres="+apres);
            }
            for(Product product:service.getAllProducts())
                System.out.printf("%d | %s | %.2f%n",product.getId(),product.getName(),product.getPrice());
        }
    }
}
