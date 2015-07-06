package cn.godzilla.rpc.util;

public class TimeUtil {
	private static volatile long current = System.currentTimeMillis();
	
	static {
		Thread thread = new Thread(new Runnable () {
			
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
						current = System.currentTimeMillis();
					} catch(InterruptedException e) {
						
					}
				}
			}
		});
	}
	
	public static long currentTime() {
		return current;
	}
}
