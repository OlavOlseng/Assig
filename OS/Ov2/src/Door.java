import java.util.Random;


public class Door implements Runnable{
	
	float dt, current_time;
	int count;
	Random rand;
	
	public Door() {
		rand = new Random(System.currentTimeMillis());
		count = 1;
	}
	
	@Override
	public void run() {
		while(SushiBar.isOpen) {
			new Thread(new Customer(count++)).start();
			try{Thread.sleep(SushiBar.doorWait);}
			catch (Exception e) {System.out.println("Something went terribly wrong!! Door broke!!"); break;}
		}
	}
}
