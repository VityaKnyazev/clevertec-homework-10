package ru.clevertec.knyazev.dao.connection;

import java.util.HashMap;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AppConnectionConfig {
	private YAMLParser yamlParser;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	private static final String DB_PROPERTY_FILE = "application.yaml";
	
	private static AppConnectionConfig appConnectionConfig;
	
	
	private AppConnectionConfig(String dbPropertyFile) {
		yamlParser = new YAMLParser(dbPropertyFile);
		entityManagerFactory = entityManagerFactory();
	}	
	
	
	private DataSource hikariDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(yamlParser.getProperty("db", "driverClassName"));
		hikariConfig.setJdbcUrl(yamlParser.getProperty("db", "jdbcUrl"));
		hikariConfig.setUsername(yamlParser.getProperty("db", "username"));
		hikariConfig.setPassword(yamlParser.getProperty("db", "password"));
		hikariConfig.setMaximumPoolSize(Integer.valueOf(yamlParser.getProperty("db", "maxPoolSize")));
		hikariConfig.setConnectionTimeout(Long.valueOf(yamlParser.getProperty("db", "connectionTimeout")));
		
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}
	
	
	private EntityManagerFactory entityManagerFactory() {
		final HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("jakarta.persistence.jtaDataSource", hikariDataSource());
		properties.put("jakarta.persistence.provider", yamlParser.getProperty("db", "hibernate", "provider"));
		
		properties.put("jakarta.persistence.schema-generation.database.action", yamlParser.getProperty("db", "schema", "generationAction"));
		properties.put("jakarta.persistence.schema-generation.scripts.create-target", yamlParser.getProperty("db", "schema", "createTablesScript"));
		properties.put("jakarta.persistence.schema-generation.scripts.drop-target", yamlParser.getProperty("db", "schema", "dropTablesScript"));
		properties.put("jakarta.persistence.sql-load-script-source", yamlParser.getProperty("db", "schema", "insertDataScript"));
		
		properties.put("hbm2ddl.auto", yamlParser.getProperty("db", "hibernate", "schema"));
		properties.put("hibernate.show.sql", yamlParser.getProperty("db", "hibernate", "showSql"));
		properties.put("hibernate.dialect", yamlParser.getProperty("db", "hibernate", "dialect"));
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("servlet-app", properties);
		return entityManagerFactory;
	}
	
	public static AppConnectionConfig getInstance() {
		if (appConnectionConfig == null) {
			synchronized (AppConnectionConfig.class) {
				if (appConnectionConfig == null) {
					appConnectionConfig = new AppConnectionConfig(DB_PROPERTY_FILE);
				}
			}			
		}
		
		return appConnectionConfig;
	}
	
	public EntityManager getEntityManager() {
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = entityManagerFactory.createEntityManager();
		}
		
		return entityManager;
	}
	
	public void closeEntityManagerFactory() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
}