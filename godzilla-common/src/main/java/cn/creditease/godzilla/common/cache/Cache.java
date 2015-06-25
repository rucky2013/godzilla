package cn.creditease.godzilla.common.cache;

public interface Cache {
	
	public void save(KV kv) ;
	
	public KV get(KV k);
	
	public void delete(KV k);
	
	class KV {
		protected String key;
		protected String value;
		
		protected KV() {}
		protected KV(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
