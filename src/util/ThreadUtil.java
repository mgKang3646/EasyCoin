package util;

public class ThreadUtil {
	
	public static void sleepThread(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
