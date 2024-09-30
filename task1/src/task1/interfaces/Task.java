package task1.interfaces;

public abstract class Task extends Thread{
	protected Task(Broker b, Runnable r) {};
	protected static Broker getBroker() {
		return null;};
}
