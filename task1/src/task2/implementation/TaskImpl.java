package task2.implementation;

import task1.interfaces.Broker;
import task2.interfaces.*;

public class TaskImpl extends Task{

	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
	}
	public TaskImpl(QueueBroker b, Runnable r) {
		super(b, r);
	}
	public Broker getBroker() {
		return null;
	}

	public QueueBroker getQueueBroker() {
		return null;
	}

	public static Task getTask() {
		return null;
	}

}
