package cn.godzilla.common.cache;

import cn.godzilla.common.cache.Cache.Entry;

public interface Cache<K, V, T> {
	
	public void save(Entry<K, V> entry);
	public void delete(K key);
	public V get(K key);
	
	public Entry<K, V> createEntry(T type);
	
	interface Entry<K, V> {
		
		public K getKey() ;
		public void setKey(K key) ;
		public V getValue() ;
		public void setValue(V value) ;
	}

}
