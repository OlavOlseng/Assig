
public class ServingArea {
	int orders, takeaways, capacity;
	private volatile int currentlySeated;
	
	public ServingArea(int capacity) {
		this.capacity = capacity;
		currentlySeated = 0;
	}
	
	public synchronized void order(int orders, int takeaways) {
		this.orders += orders;
		this.takeaways += takeaways;
	}
	
	public synchronized void getSeat() {
		while(currentlySeated >= capacity) {
			try {
				SushiBar.write("Capacity full!");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		currentlySeated++;
	}
	
	public synchronized void getUp() {
		if(currentlySeated-- == capacity);
		notify();
	}
}
