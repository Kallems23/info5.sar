package task3.implementation;

public class Task {
	
	public Task() {
	}
	public void post(Runnable r) {
		EventPump.getPump().post(r);
	}
	
	public static Task task() {
		return null;
		
	}
	public void kill() {
		
	}
	public boolean killed() {
		return false;
		
	}
	}
