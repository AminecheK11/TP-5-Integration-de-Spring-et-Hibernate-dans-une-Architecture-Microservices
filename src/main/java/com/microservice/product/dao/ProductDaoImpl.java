package com.microservice.product.dao;
import com.microservice.product.entity.Product;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class ProductDaoImpl implements IDao<Product> {
    @Autowired private SessionFactory sessionFactory;
    public void save(Product product) { sessionFactory.getCurrentSession().save(product); }
    public void update(Product product) { sessionFactory.getCurrentSession().update(product); }
    public void delete(Product product) { sessionFactory.getCurrentSession().delete(product); }
    public Product findById(Long id) { return sessionFactory.getCurrentSession().get(Product.class,id); }
    public List<Product> findAll() { return sessionFactory.getCurrentSession().createQuery("FROM Product",Product.class).list(); }
}
