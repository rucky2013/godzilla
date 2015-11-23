package cn.godzilla.common.cache;


public interface Cache<K, V, T> {
	
	public void save(Entry<K, V> entry);
	public void delete(K key);
	public V get(K key);
	
	public Entry<K, V> createEntry(T type) ;
	
	interface Entry<K, V> {
		
		public K getKey() ;
		public Entry setKey(K key) ;
		public V getValue() ;
		public Entry setValue(V value) ;
	}

}
