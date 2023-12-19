package ru.clevertec.knyazev.cache;

import ru.clevertec.knyazev.util.YAMLParser;

/**
 * Represents abstract cache factory
 */
public abstract class AbstractCacheFactory {

    private static final String PROPERTY_FILE = "application.yaml";

    protected String CACHE_ALGORITHM;
    protected Integer DEFAULT_CACHE_SIZE;

    public AbstractCacheFactory(String cacheAlgorithm, Integer cacheSize) {
        CACHE_ALGORITHM = cacheAlgorithm;
        DEFAULT_CACHE_SIZE = cacheSize;
    }

    /**
     *
     * Init cache using determined cache algorithm
     *
     * @return cache that uses determined cache algorithm
     * @param <K> cache key type
     * @param <V> cache value type
     */
    public abstract <K, V> Cache<K,V> initCache();
}
