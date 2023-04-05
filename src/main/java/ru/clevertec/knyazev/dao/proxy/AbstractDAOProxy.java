package ru.clevertec.knyazev.dao.proxy;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.cache.SimpleCacheFactory;

public abstract class AbstractDAOProxy<K, V> implements InvocationHandler {
	private static final String CACHE_FILE_SETTINGS = "application.yaml";
	private static final String CACHE_OBJECT = "cache";
	private static final String CACHE_ALGORITHM = "algorithm";
	private static final String CACHE_MAX_SIZE = "size";
	
	Cache<K, V> cache;
	
	AbstractDAOProxy(SimpleCacheFactory<K, V> cacheFactory) {
		Yaml yaml = new Yaml();
		InputStream cacheFileSettingsStream = this.getClass().getClassLoader().getResourceAsStream(CACHE_FILE_SETTINGS);
		Map<String, Object> yamlProperties =  yaml.load(cacheFileSettingsStream);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> cacheProperties = (Map<String, Object>) yamlProperties.get(CACHE_OBJECT);
		String algorithm = (String) cacheProperties.get(CACHE_ALGORITHM);
		Integer cacheSize = (Integer) cacheProperties.get(CACHE_MAX_SIZE);
		cache = cacheFactory.initCache(algorithm, cacheSize);		
	}
	
	
}
