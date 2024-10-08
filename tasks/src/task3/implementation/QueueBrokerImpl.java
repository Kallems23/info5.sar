package task3.implementation;

import task1.implementation.BrokerImpl;
import task1.implementation.TaskImpl;
import task1.interfaces.Broker;
import task3.interfaces.MessageQueue;
import task3.interfaces.QueueBroker;

public class QueueBrokerImpl extends QueueBroker {
	Broker m_br;

	public QueueBrokerImpl(String name) {
		super(name);
		m_br = new BrokerImpl(name);
	}

	@Override
	public boolean bind(int port, AcceptListener listener) {
		Task acceptTask = new Task();
		acceptTask.post(new Runnable() {

			@Override
			public void run() {
				TaskImpl t = new TaskImpl(m_br, new Runnable() {
					
					@Override
					public void run() {
						MessageQueue mqueue = new MessageQueueImpl(m_br.accept(port));
						acceptTask.post(new Runnable() {

							@Override
							public void run() {
								listener.accepted(mqueue);
							}
						});
					}
				});
			}

		});
		return true;
	}

	@Override
	public boolean unbind(int port) {
		return false;
	}

	@Override
	public boolean connect(String name, int port, ConnectListener listener) {
		Task connectTask = new Task();

		connectTask.post(new Runnable() {

			@Override
			public void run() {
				TaskImpl t = new TaskImpl(m_br, new Runnable() {

					@Override
					public void run() {
						MessageQueue mqueue = new MessageQueueImpl(m_br.connect(name, port));
						connectTask.post(new Runnable() {

							@Override
							public void run() {
								listener.connected(mqueue);
							}
						});
					}
				});
			}

		});
		return true;
	}

}
