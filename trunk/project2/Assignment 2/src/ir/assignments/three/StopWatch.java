package ir.assignments.three;

public class StopWatch {
	private long startTime;
	private long endTime;
	
	public void start() {
		this.startTime = System.nanoTime();
	}
	
	public void stop() {
		this.endTime = System.nanoTime();
	}
	
	public double getTotalElapsedSeconds() {
		long elapsedNanoSeconds = this.endTime - this.startTime;
		return (double)elapsedNanoSeconds / 1000000000.0; // 1 nanosecond = 1e-9 second
	}
}
