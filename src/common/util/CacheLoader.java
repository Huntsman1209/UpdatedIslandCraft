package common.util;

public interface CacheLoader<K, V> {
	V load(K key);
}
