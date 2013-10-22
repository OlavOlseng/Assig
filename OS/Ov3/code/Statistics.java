/**
 * This class contains a lot of public variables that can be updated
 * by other classes during a simulation, to collect information about
 * the run.
 */
public class Statistics
{
	/** The number of processes that have exited the system */
	public long nofCompletedProcesses = 0;
	/** The number of processes that have entered the system */
	public long nofCreatedProcesses = 0;
	/** The total time that all completed processes have spent waiting for memory */
	public long totalTimeSpentWaitingForMemory = 0;
	/** The time-weighted length of the queue, divide this number by the total time to get average queue length */
	public long memoryQueueLengthTime = 0;
	public long cpuQueueLengthTime = 0;
	public long IOQueueLengthTime = 0;

	/** The largest memory length that has occured */
	public long memoryQueueLargestLength = 0;
	public long cpuQueueLargestLength = 0;
	public long IOqueueLargestLength = 0;
	
	//Some of our variables
	public long cpuIdleTime = 0;
	public long cpuProcessTime = 0;
	
	public long totalTimeSpentWaitingForCPU = 0; 
	public long totalTimeSpentWaitingForIO = 0;
	public long totalTimeSpentProcessing = 0;
	public long totalTimeSpentInIO = 0;
	
	public long totalEntriesInCPUqueue = 0;
	public long totalEntriesInIOqueue = 0;
	
	public long noOfForcedProcessSwitches = 0;
	public long noOfProcessedIOOps = 0;
	
	/**
	 * Prints out a report summarizing all collected data about the simulation.
	 * @param simulationLength	The number of milliseconds that the simulation covered.
	 */
	public void printReport(long simulationLength) {
		System.out.println();
		System.out.println("Simulation statistics:");
		System.out.println();
		System.out.println("Number of completed processes:                                "+nofCompletedProcesses);
		System.out.println("Number of created processes:                                  "+nofCreatedProcesses);
		System.out.println("Number of (forced) process switches:                          "+noOfForcedProcessSwitches);
		System.out.println("Number of processed I/O operations:                           "+noOfProcessedIOOps);
		System.out.println("Average throughput (processes per second):                    "+(float)nofCompletedProcesses/simulationLength*1000);
		System.out.println();
		System.out.println("Total CPU time spent processing:                              "+cpuProcessTime+" ms");
		System.out.println("Fraction of CPU time spent processing:                        "+(float)cpuProcessTime/(float)(cpuIdleTime+cpuProcessTime)+" ms");
		System.out.println("Total CPU time spent waiting:                                 "+cpuIdleTime+" ms");
		System.out.println("Fraction of CPU time spent waiting:                           "+(float)cpuIdleTime/(cpuIdleTime+cpuProcessTime)+" ms");
		System.out.println();
		System.out.println("Largest occuring memory queue length:                         "+memoryQueueLargestLength);
		System.out.println("Average memory queue length:                                  "+(float)memoryQueueLengthTime/simulationLength);
		System.out.println("Largest occuring cpu queue length:                            "+cpuQueueLargestLength);
		System.out.println("Average cpu queue length:                                     "+(float)cpuQueueLengthTime/simulationLength);
		System.out.println("Largest occuring I/O queue length:                            "+IOqueueLargestLength);
		System.out.println("Average I/O queue length:                                     "+IOQueueLengthTime/simulationLength);
		if(nofCompletedProcesses > 0) {
			System.out.println("Average # of times a process has been placed in memory queue: "+1);
			System.out.println("Average # of times a process has been placed in cpu queue:    "+ (float)totalEntriesInCPUqueue/nofCreatedProcesses);
			System.out.println("Average # of times a process has been placed in I/O queue:    "+ (float)totalEntriesInIOqueue/nofCreatedProcesses);
			System.out.println();
			System.out.println("Average time spent in system per process:                     ");
			System.out.println("Average time spent waiting for memory per process:            "+
				totalTimeSpentWaitingForMemory/nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for cpu per process:               "+totalTimeSpentWaitingForCPU/nofCreatedProcesses+" ms");
			System.out.println("Average time spent waiting for I/O per process:               "+totalTimeSpentWaitingForIO/nofCreatedProcesses+" ms");
			System.out.println();
			System.out.println("Average time spent processing per process:                    "+totalTimeSpentProcessing/nofCreatedProcesses+" ms");
			System.out.println("Average time spent in I/O per process:                        "+totalTimeSpentInIO/nofCreatedProcesses+" ms");
		}
	}
	
	//Our code
	
}
