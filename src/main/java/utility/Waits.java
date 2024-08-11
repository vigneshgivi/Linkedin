package utility;

public class Waits {

	public static void normalwait(long waitTime) {
		try {
			Thread.sleep(waitTime);
			System.out.println("normalWait : " + waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
