package com.example.util;


import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.example.beans.Homme;
import com.example.beans.Femme;
import com.example.beans.Mariage;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties props = new Properties();
                props.load(HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties"));

                // Hibernate core properties mapping
                configuration.setProperty("hibernate.connection.driver_class", props.getProperty("db.driver"));
                configuration.setProperty("hibernate.connection.url", props.getProperty("db.url"));
                configuration.setProperty("hibernate.connection.username", props.getProperty("db.username"));
                configuration.setProperty("hibernate.connection.password", props.getProperty("db.password"));
                configuration.setProperty("hibernate.dialect", props.getProperty("hibernate.dialect"));
                configuration.setProperty("hibernate.hbm2ddl.auto", props.getProperty("hibernate.hbm2ddl.auto"));
                configuration.setProperty("hibernate.show_sql", props.getProperty("hibernate.show_sql"));
                configuration.setProperty("hibernate.format_sql", props.getProperty("hibernate.format_sql"));

                // add annotated classes
                configuration.addAnnotatedClass(Homme.class);
                configuration.addAnnotatedClass(Femme.class);
                configuration.addAnnotatedClass(Mariage.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("SessionFactory creation failed: " + e.getMessage());
            }
        }
        return sessionFactory;
    }
}

