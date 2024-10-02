package task2.interfaces;

import task1.interfaces.Broker;

public abstract class Task extends Thread {
	public Task(Broker b, Runnable r) {super(r);}

	public Task(QueueBroker b, Runnable r) {super(r);}

	public abstract Broker getBroker();

	public abstract QueueBroker getQueueBroker();

	public static Task getTask() {
		return null;
	}
}