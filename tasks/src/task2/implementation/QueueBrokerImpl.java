package task2.implementation;

import java.util.concurrent.TimeoutException;

import task1.interfaces.Broker;
import task2.interfaces.MessageQueue;
import task2.interfaces.QueueBroker;

public class QueueBrokerImpl extends QueueBroker {
	
	Broker m_br;
	
	public QueueBrokerImpl(Broker broker) {
		super(broker);
		m_br = broker;
	}

	@Override
	public String name() {
		return m_br.name();
	}

	@Override
	public MessageQueue accept(int port) {
		return new MessageQueueImpl(m_br.accept(port));
	}

	@Override
	public MessageQueue connect(String name, int port) throws TimeoutException {
		return new MessageQueueImpl(m_br.connect(name, port));
	}

}
