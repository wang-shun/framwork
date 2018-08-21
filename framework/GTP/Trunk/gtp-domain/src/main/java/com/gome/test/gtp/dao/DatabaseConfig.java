/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 *
 * @author Zonglin.Li
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

  // ==============
    // PRIVATE FIELDS
    // ==============
    @Autowired
    private AppConfiguration env;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

  // ==============
    // PUBLIC METHODS
    // ==============
    /**
     * DataSource definition for database connection. Settings are read from the
     * application.properties file (using the env object).
     *
     * @return
     */
    @Bean
    public DataSource dataSource() throws Exception {
        /**
         * 带连接池的BoneCPDataSource
         */



//        BoneCPDataSource dataSource = new BoneCPDataSource();
//        InputStream is = this.getClass().getResourceAsStream("/boneCP.properties");
//        Properties boneProps = new Properties();
//        boneProps.load(is);
//        dataSource.setProperties(boneProps);


//        dataSource.setDriverClass(env.getProperty("db.driver"));
//        dataSource.setJdbcUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));
//
//        dataSource.setIdleConnectionTestPeriodInMinutes(Integer.valueOf(env.getProperty("bonecp.idleConnectionTestPeriodInMinutes")));
//        dataSource.setIdleMaxAgeInMinutes(Integer.valueOf(env.getProperty("bonecp.idleMaxAgeInMinutes")));
//        dataSource.setMaxConnectionsPerPartition(Integer.valueOf(env.getProperty("bonecp.maxConnectionsPerPartition")));
//        dataSource.setMinConnectionsPerPartition(Integer.valueOf(env.getProperty("bonecp.minConnectionsPerPartition")));
//        dataSource.setStatementsCacheSize(Integer.valueOf(env.getProperty("bonecp.statementsCacheSize")));
//        dataSource.setPartitionCount(Integer.valueOf(env.getProperty("bonecp.partitionCount")));
//        dataSource.setAcquireIncrement(Integer.valueOf(env.getProperty("bonecp.acquireIncrement")));

        /**
         * 不带连接池的DataSource
         */
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("db.driver"));
//        dataSource.setUrl(env.getProperty("db.url"));
//        dataSource.setUsername(env.getProperty("db.username"));
//        dataSource.setPassword(env.getProperty("db.password"));

        /**
         * 外部Tomcat使用的DataSource（BoneCP,C3P tomcat报错）
         */
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setInitialSize(Integer.valueOf(env.getProperty("db.initialSize")));
        dataSource.setMaxActive(Integer.valueOf(env.getProperty("db.maxActive")));
        dataSource.setMaxIdle(Integer.valueOf(env.getProperty("db.maxIdle")));
        dataSource.setMinIdle(Integer.valueOf(env.getProperty("db.minIdle")));
        dataSource.setMaxWait(Long.valueOf(env.getProperty("db.maxWait")));
        dataSource.setLogAbandoned(Boolean.valueOf(env.getProperty("db.logAbandoned")));
        dataSource.setRemoveAbandoned(Boolean.valueOf(env.getProperty("db.removeAbandoned")));
        dataSource.setRemoveAbandonedTimeout(Integer.valueOf(env.getProperty("db.removeAbandonedTimeout")));
        return dataSource;
    }

    /**
     * Declare the JPA entity manager factory.
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory
                = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);

        // Classpath scanning of @Component, @Service, etc annotated class
        entityManagerFactory.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));

        // Vendor adapter
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        // Hibernate properties
        Properties additionalProperties = new Properties();
        additionalProperties.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
        additionalProperties.put("hibernate.show_sql",env.getProperty("hibernate.show_sql"));
        additionalProperties.put("hibernate.hbm2ddl.auto",env.getProperty("hibernate.hbm2ddl.auto"));
        entityManagerFactory.setJpaProperties(additionalProperties);
  
        return entityManagerFactory;
    }

    /**
     * Declare the transaction manager.
     *
     * @return
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    /**
     * PersistenceExceptionTranslationPostProcessor is a bean post processor
     * which adds an advisor to any bean annotated with Repository so that any
     * platform-specific exceptions are caught and then rethrown as one Spring's
     * unchecked data access exceptions (i.e. a subclass of
     * DataAccessException).
     *
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

} // class DatabaseConfig
