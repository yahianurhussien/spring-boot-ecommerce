package com.mayah.ecommerce.config;

import com.mayah.ecommerce.entity.Product;
import com.mayah.ecommerce.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {


    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = { HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE };

        //disable HTTP methods for product : PUT POST DELETE
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));


        //disable HTTP methods for product-category : PUT POST DELETE
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

       // RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();
        for (EntityType<?> entityType : entityTypes) {

            entityClasses.add(entityType.getJavaType());
        }


        Class[] domainTypes = entityClasses.toArray(new Class[entityClasses.size()]);
        config.exposeIdsFor(domainTypes);
    }
}
