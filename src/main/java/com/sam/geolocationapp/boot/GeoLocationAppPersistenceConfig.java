package com.sam.geolocationapp.boot;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sam.geolocationapp.utility.GeoLocationAppConstants;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class GeoLocationAppPersistenceConfig {
	
	@Value("${com.sam.geolocation.hibernate.dialect}")
	protected String hibernateDialet;
	
	@Value("${com.sam.geolocation.hibernate.showSql}")
	protected String hibernateShowSql;
	
	@Value("${com.sam.geolocation.hibernate.sqlComments}")
	protected String hibernateSqlComments;
	
	@Value("${com.sam.geolocation.hibernate.sqlFormat}")
	protected String hibernateSqlFormat;
	
	@Value("${com.sam.geolocation.hibernate.hbm2ddl}")
	protected String hibernateHbm2Ddl;
	
	@Value("${com.sam.geolocation.hibernate.statistic}")
	protected String hibernateStatistic;
	
	@Value("${com.sam.geolocation.hibernate.enitityPackage}")
	protected String hibernateEntityPackage;

	@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        
        return transactionManager;
    }
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(hsqlDataSource());
        emf.setPackagesToScan(new String[] {hibernateEntityPackage});

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(buildHibernateProperties());

       return emf;
    }
	
	@Bean
    public DataSource hsqlDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
        									.build();
    }
	
	protected Properties buildHibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_DIALECT_KEY, hibernateDialet);
        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_SHOW_SQL_KEY, hibernateShowSql);
        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_USE_SQL_COMMENTS_KEY, hibernateSqlComments);
        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_FORMAT_SQL_KEY, hibernateSqlFormat);
        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_HBM2DDL_AUTO_KEY, hibernateHbm2Ddl);
        hibernateProperties.setProperty(GeoLocationAppConstants.HIBERNATE_GENERATE_STATISTICS_KEY, hibernateStatistic);

        return hibernateProperties;
    }
}
