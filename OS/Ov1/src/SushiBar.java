import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SushiBar {
	//SushiBar settings:
    	//These parameters have to be changed to check that the program works in all situations.
	private static int capacity =5; //capacity of Sushi Bar
	private static int duration = 6; // Simulation time
	public static int maxOrder = 10; // Maximum number of orders for each customer
	public static int customerWait= 4000; // coefficient of eating time for customers
	public static int doorWait = 200; // coefficient of waiting time for door for creating next customer
	public static boolean isOpen=true;
	private static Door d;
	public static ServingArea servingArea = new ServingArea(capacity);
	
	
	//Creating the log file
	private static File log;
	private static String path = "./";
        
	public static void main(String[] args) {
		log= new File(path + "log.txt"); 
		new Clock(duration);
		d = new Door();
		new Thread(d).start();
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while(Thread.activeCount() > 1) {Thread.yield();}
		write("Customers served: " + (d.count - 1));
		write("Takeaway orders : " + servingArea.takeaways);
		write("Total orders : " + servingArea.orders);
	}	
	
	
	//Writes actions in the log file and console
	public static void write(String str){
		try {
			FileWriter fw = new FileWriter(log.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(Clock.getTime() + ", " + str +"\n");
			bw.close();
			System.out.println(Clock.getTime() + ", " + str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
