package task2.implementation;

import task1.interfaces.Broker;
import task2.interfaces.*;

public class TaskImpl extends Task {
	Broker m_brk;
	QueueBroker m_qbrk;
	
	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
		m_brk = b;
		this.start();
	}

	public TaskImpl(QueueBroker b, Runnable r) {
		super(b, r);
		m_qbrk = b;
		this.start();
	}

	public Broker getBroker() {
		return m_brk;
	}

	public QueueBroker getQueueBroker() {
		return m_qbrk;
	}
	
	public static Task getTask() {
		if (currentThread() instanceof TaskImpl)
			return (TaskImpl) currentThread();
		return null;
	}

}
