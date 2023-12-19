package ru.clevertec.knyazev.cache.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.knyazev.cache.AbstractCacheFactory;
import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.dao.proxy.PersonDaoProxy;
import ru.clevertec.knyazev.util.YAMLParser;

import java.util.Locale;

/**
 * Represents cache factory that instantiate cache on LRU or
 * LFU algorithm
 */
public class DefaultCacheFactory extends AbstractCacheFactory {

    private static final String INSTANTIATION_CACHE_ERROR = "Error when instantiating cache";

    public DefaultCacheFactory(String cacheAlgorithm, Integer cacheSize) {
        super(cacheAlgorithm, cacheSize);
    }

    /**
     *
     * Instantiate cache that use LRU or LFU algorithm
     *
     * @return cache
     * @param <K> cache key type
     * @param <V> cache value type
     */
    @Override
    public <K, V> Cache<K, V> initCache() {
        CacheAlgorithm algorithm = CacheAlgorithm.findByName(CACHE_ALGORITHM.toUpperCase(Locale.getDefault()));

        return switch (algorithm) {
            case LFU -> new LFUCache<>(DEFAULT_CACHE_SIZE);
            case LRU -> new LRUCache<>(DEFAULT_CACHE_SIZE);
            default -> throw new IllegalArgumentException(INSTANTIATION_CACHE_ERROR);
        };
    }

    /**
     * Represents types of cache algorithms
     */
    private enum CacheAlgorithm {
        LRU, LFU, undefined;

        public static CacheAlgorithm findByName(String name) {

            CacheAlgorithm result = null;
            for (CacheAlgorithm cacheAlgorithmType : values()) {

                if (cacheAlgorithmType.name().equalsIgnoreCase(name)) {
                    result = cacheAlgorithmType;
                    break;
                }

            }

            return result != null ? result : undefined;
        }
    }
}
