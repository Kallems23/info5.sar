package task1.implementation;

import task1.interfaces.Broker;
import task1.interfaces.Task;

public class TaskImpl extends Task {
	
	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
		this.start();
	};
	public static Broker getBroker() { return null; };
}

