package com.microservice.product.service;
import com.microservice.product.dao.IDao;
import com.microservice.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired private IDao<Product> productDao;
    private void validate(Product product) {
        if(product==null) throw new IllegalArgumentException("Produit obligatoire");
        if(!Double.isFinite(product.getPrice()) || product.getPrice()<0)
            throw new IllegalArgumentException("Le prix doit etre fini et non negatif");
        if(product.getName()==null || product.getName().isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire");
    }
    @Transactional public void addProduct(Product product) { validate(product); productDao.save(product); }
    @Transactional public void updateProduct(Product product) {
        validate(product);
        if(product.getId()==null) throw new IllegalArgumentException("Identifiant obligatoire");
        productDao.update(product);
    }
    @Transactional public void deleteProduct(Product product) {
        if(product==null || product.getId()==null) throw new IllegalArgumentException("Produit persiste obligatoire");
        productDao.delete(product);
    }
    @Transactional(readOnly=true) public Product getProductById(Long id) { return productDao.findById(id); }
    @Transactional(readOnly=true) public List<Product> getAllProducts() { return productDao.findAll(); }
    /** Une seule transaction pour le lot : une erreur annule aussi les premieres insertions. */
    @Transactional public void addProducts(List<Product> products) {
        for(Product product:products) { validate(product); productDao.save(product); }
    }
}
