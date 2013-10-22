
public class CPU {

	Statistics statistics;
	private Process currentProcess;
	long lastEventTime;
	Gui gui;
	
	public CPU(long clock, Statistics statistics, Gui gui){
		this.statistics = statistics;
		this.lastEventTime = clock;
		this.gui = gui;
	}
	
	public void setProcess(Process p, long clock) {
		if(currentProcess == null) {
			statistics.cpuIdleTime += clock - lastEventTime;
		}
		lastEventTime = clock;
		this.currentProcess = p;
		gui.setCpuActive(p);
	}
	
	public Process removeProcess(long clock) {
		if(currentProcess == null) return null;
		
		Process p = currentProcess;
		p.leaveCPU(clock);
		currentProcess = null;
		statistics.cpuProcessTime += clock - lastEventTime;
		lastEventTime = clock;
		gui.setCpuActive(null);
		return p;
	}
}
