package task1.implementation;

import task1.interfaces.Broker;

public class TaskImpl extends Thread {
	
	public TaskImpl(Broker b, Runnable r) {
		super(r);
		this.start();
	};
	public static Broker getBroker() { return null; };
}

