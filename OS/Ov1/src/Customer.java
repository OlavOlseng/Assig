import java.util.Random;


public class Customer implements Runnable{
	private static Random rand = new Random(System.currentTimeMillis());
	
	final public int ID;
	private int orders, takeaways; 
	
	public Customer(int ID) {
		this.ID = ID;
		orders =  rand.nextInt(SushiBar.maxOrder) + 1;
		takeaways = rand.nextInt(orders + 1);	
	}
	
	public int getID() {
		return this.ID;
	} 
	
	public int getOrders() {
		return orders;
	}
	
	public int getTakeaway() {
		return takeaways;
	}
	
	public int getEatins() {
		return orders - takeaways;
	}
	
	
	@Override
	public void run() {
		SushiBar.write(Thread.currentThread().getName() +": Customer " + this.ID + " entered the bar.");
		SushiBar.servingArea.order(orders, takeaways);
		if(getEatins() > 0) {
			SushiBar.servingArea.getSeat();
			SushiBar.write(Thread.currentThread().getName() +": Customer " + this.ID + " grabbed a seat.");
			
			try {
				Thread.sleep(SushiBar.customerWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			SushiBar.servingArea.getUp();
			SushiBar.write(Thread.currentThread().getName() +": Customer " + this.ID + " left a seat.");
		}
		
		else {
			SushiBar.write(Thread.currentThread().getName() +": Customer " + this.ID + " only had takeaway.");
			
		}
		
		SushiBar.write(Thread.currentThread().getName() +": Customer " + this.ID + " has left the bar.");
	}
}
