import java.io.*;

/**
 * The main class of the P3 exercise. This class is only partially complete.
 */
public class Simulator implements Constants
{
	/** The queue of events to come */
    private EventQueue eventQueue;
	/** Reference to the memory unit */
    private Memory memory;
	/** Reference to the GUI interface */
	private Gui gui;
	/** Reference to the statistics collector */
	private Statistics statistics;
	/** The global clock */
    private long clock;
	/** The length of the simulation */
	private long simulationLength;
	/** The average length between process arrivals */
	private long avgArrivalInterval;
	
	// Add member variables as needed
	private CPU cpu;
	private Queue cpuQueue;
	private Queue ioQueue;
	private long maxCpuTime, avgIoTime;
	
	/**
	 * Constructs a scheduling simulator with the given parameters.
	 * @param memoryQueue			The memory queue to be used.
	 * @param cpuQueue				The CPU queue to be used.
	 * @param ioQueue				The I/O queue to be used.
	 * @param memorySize			The size of the memory.
	 * @param maxCpuTime			The maximum time quant used by the RR algorithm.
	 * @param avgIoTime				The average length of an I/O operation.
	 * @param simulationLength		The length of the simulation.
	 * @param avgArrivalInterval	The average time between process arrivals.
	 * @param gui					Reference to the GUI interface.
	 */
	public Simulator(Queue memoryQueue, Queue cpuQueue, Queue ioQueue, long memorySize,
			long maxCpuTime, long avgIoTime, long simulationLength, long avgArrivalInterval, Gui gui) {
		this.simulationLength = simulationLength;
		this.avgArrivalInterval = avgArrivalInterval;
		this.gui = gui;
		statistics = new Statistics();
		eventQueue = new EventQueue();
		memory = new Memory(memoryQueue, memorySize, statistics);
		clock = 0;

		// Add code as needed
		cpu = new CPU(clock, statistics, gui);
		this.cpuQueue = cpuQueue;
		this.ioQueue = ioQueue;
		this.avgIoTime = avgIoTime;
		this.maxCpuTime = maxCpuTime;
	}

    /**
	 * Starts the simulation. Contains the main loop, processing events.
	 * This method is called when the "Start simulation" button in the
	 * GUI is clicked.
	 */
	public void simulate() {
		// TODO: You may want to extend this method somewhat.

		System.out.print("Simulating...");
		// Genererate the first process arrival event
		eventQueue.insertEvent(new Event(NEW_PROCESS, 0));
		eventQueue.insertEvent(new Event(SWITCH_PROCESS, 1));
		// Process events until the simulation length is exceeded:
		while (clock < simulationLength && !eventQueue.isEmpty()) {
			// Find the next event
			Event event = eventQueue.getNextEvent();
			// Find out how much time that passed...
			long timeDifference = event.getTime()-clock;
			// ...and update the clock.
			clock = event.getTime();
			// Let the memory unit and the GUI know that time has passed
			memory.timePassed(timeDifference);
			gui.timePassed(timeDifference);
			
			//This updates the time passed on the IO device and the CPU, for statistics
			timePassed(timeDifference);
			
			// Deal with the event
			if (clock < simulationLength) {
				processEvent(event);
			}

			// Note that the processing of most events should lead to new
			// events being added to the event queue!
			

		}
		System.out.println("..done.");
		// End the simulation by printing out the required statistics
		statistics.printReport(simulationLength);
	}

	/**
	 * Processes an event by inspecting its type and delegating
	 * the work to the appropriate method.
	 * @param event	The event to be processed.
	 */
	private void processEvent(Event event) {
		switch (event.getType()) {
			case NEW_PROCESS:
				createProcess();
				break;
			case END_PROCESS:
				endProcess();
				break;
			case SWITCH_PROCESS:
				switchProcess();
				break;
			case IO_REQUEST:
				processIoRequest();
				break;
			case END_IO:
				endIoOperation();
				break;
		}
	}

	/**
	 * Simulates a process arrival/creation.
	 */
	private void createProcess() {
		// Create a new process
		Process newProcess = new Process(memory.getMemorySize(), clock);
		memory.insertProcess(newProcess);
		
		//update statistics
		statistics.nofCreatedProcesses++;
		
		flushMemoryQueue();
		// Add an event for the next process arrival
		long nextArrivalTime = clock + 1 + (long)(2*Math.random()*avgArrivalInterval);
		eventQueue.insertEvent(new Event(NEW_PROCESS, nextArrivalTime));
    }

	/**
	 * Transfers processes from the memory queue to the ready queue as long as there is enough
	 * memory for the processes.
	 */
	private void flushMemoryQueue() {
		Process p = memory.checkMemory(clock);
		// As long as there is enough memory, processes are moved from the memory queue to the cpu queue
		while(p != null) {
			
			// TODO: Add this process to the CPU queue!
			// Also add new events to the event queue if needed
			cpuQueue.insert(p);
			
			// Since we haven't implemented the CPU and I/O device yet,
			// we let the process leave the system immediately, for now.
//			memory.processCompleted(p);
			
			// Check for more free memory
			p = memory.checkMemory(clock);
		}
	}

	/**
	 * Simulates a process switch.
	 */
	private void switchProcess() {
		System.out.println("Switching process");
		//Remove current process from CPU
		Process p = cpu.removeProcess(clock);
		if(p != null) {
			cpuQueue.insert(p);
			statistics.noOfForcedProcessSwitches++;
		}
		
		//let next process enter CPU
		if(!cpuQueue.isEmpty()) {
			p = (Process) (cpuQueue.getNext());
			cpuQueue.removeNext();
			enterCPU(p);
		}	
		eventQueue.insertEvent(new Event(SWITCH_PROCESS, clock + maxCpuTime));
	}
	
	public void enterCPU(Process p) {
		cpu.setProcess(p, clock);
		p.leaveReadyQueue(clock);
		System.out.println("clock: "+clock+" , p.getCpuTimeLeft(): "+p.getCpuTimeLeft());
		if(p.getCpuTimeLeft() < maxCpuTime){
			//the process finishes within the current timeframe; an end process call is needed
			eventQueue.insertEvent(new Event(END_PROCESS, clock + p.getCpuTimeLeft()));
		}
		else if (p.needsIoInInterval(clock, maxCpuTime)){
			eventQueue.insertEvent(new Event(IO_REQUEST, clock + p.getTimeToNextIoOperation()));
			//TODO : set time to varying time
			p.setTimeToNextIoOperation(p.getAvgIoInterval());
		}
		else {
			p.setTimeToNextIoOperation(p.getTimeToNextIoOperation()-maxCpuTime);
		}
	}
	
	/**
	 * Ends the active process, and deallocates any resources allocated to it.
	 */
	private void endProcess() {
		System.out.println("End process spotted");
		Process oldProcess = cpu.removeProcess(clock);
		memory.processCompleted(oldProcess);

		// Update statistics
		oldProcess.updateStatistics(statistics);
		flushMemoryQueue();
	}

	/**
	 * Processes an event signifying that the active process needs to
	 * perform an I/O operation.
	 */
	private void processIoRequest() {
		System.out.println("IOEwquest spotted");
		Process p = cpu.removeProcess(clock);
		if (ioQueue.isEmpty()) {
			startIoOperation(p);
			ioQueue.insert(p);
		}
		else {
			ioQueue.insert(p);
		}
	}

	/**
	 * Processes an event signifying that the process currently doing I/O
	 * is done with its I/O operation.
	 */
	private void endIoOperation() {
		//Finish currentIO
		statistics.noOfProcessedIOOps++;
		System.out.println("IO_End");
		gui.setIoActive(null);
		Process p;
		if(!ioQueue.isEmpty()) {
			p = (Process) ioQueue.removeNext();
			p.leaveIO(clock);
			cpuQueue.insert(p);
		}
		
		//start next IO, if there is one;
		if(!ioQueue.isEmpty()) {
			p = (Process) ioQueue.getNext();
			startIoOperation(p);
		}
	}
	
	//Method to start IO operation
	private void startIoOperation(Process p) {
		if(p != null) p.leaveIOqueue(clock);
		//TODO : Swap avgIoTime to varying variable.
		eventQueue.insertEvent(new Event(END_IO, clock + avgIoTime));
		gui.setIoActive(p);
	}
	
	private void timePassed(long timePassed) {
		statistics.cpuQueueLengthTime += cpuQueue.getQueueLength()*timePassed;
		if (cpuQueue.getQueueLength() > statistics.cpuQueueLargestLength) {
			statistics.cpuQueueLargestLength = cpuQueue.getQueueLength();
		}
		//This part needs a small hack, since we have no explicit I/O unit.
		long currentIOqueueLength = 0;
		if(ioQueue.getQueueLength() > 1) currentIOqueueLength = ioQueue.getQueueLength() - 1;
		statistics.IOQueueLengthTime += (currentIOqueueLength)*timePassed;
		
		if (currentIOqueueLength > statistics.IOqueueLargestLength) {
			statistics.IOqueueLargestLength = currentIOqueueLength;
		}
		
	}
	
	/**
	 * Reads a number from the an input reader.
	 * @param reader	The input reader from which to read a number.
	 * @return			The number that was inputted.
	 */
	public static long readLong(BufferedReader reader) {
		try {
			return Long.parseLong(reader.readLine());
		} catch (IOException ioe) {
			return 100;
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * The startup method. Reads relevant parameters from the standard input,
	 * and starts up the GUI. The GUI will then start the simulation when
	 * the user clicks the "Start simulation" button.
	 * @param args	Parameters from the command line, they are ignored.
	 */
	public static void main(String args[]) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please input system parameters: ");

		System.out.print("Memory size (KB): ");
		long memorySize = readLong(reader);
		while(memorySize < 400) {
			System.out.println("Memory size must be at least 400 KB. Specify memory size (KB): ");
			memorySize = readLong(reader);
		}

		System.out.print("Maximum uninterrupted cpu time for a process (ms): ");
		long maxCpuTime = readLong(reader);

		System.out.print("Average I/O operation time (ms): ");
		long avgIoTime = readLong(reader);

		System.out.print("Simulation length (ms): ");
		long simulationLength = readLong(reader);
		while(simulationLength < 1) {
			System.out.println("Simulation length must be at least 1 ms. Specify simulation length (ms): ");
			simulationLength = readLong(reader);
		}

		System.out.print("Average time between process arrivals (ms): ");
		long avgArrivalInterval = readLong(reader);

		SimulationGui gui = new SimulationGui(2048, 500, 500, 250000, 100);
//		SimulationGui gui = new SimulationGui(memorySize, maxCpuTime, avgIoTime, simulationLength, avgArrivalInterval);
	}
}
