package task1.implementation;

public class TaskImpl extends Thread {
	public TaskImpl(BrokerImpl b, Runnable r) {
		super(r);
		this.start();
	};
	public static BrokerImpl getBroker() { return null; };
}

