package task1.interfaces;

public abstract class Task extends Thread{
	public Task(Broker b, Runnable r) {super(r);};
	public static Broker getBroker() {
		return null;};
}
