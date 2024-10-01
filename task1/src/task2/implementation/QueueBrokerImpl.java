package task2.implementation;

import task1.interfaces.Broker;
import task2.interfaces.MessageQueue;
import task2.interfaces.QueueBroker;

public class QueueBrokerImpl extends QueueBroker {

	public QueueBrokerImpl(Broker broker) {
		super(broker);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageQueue accept(int port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageQueue connect(String name, int port) {
		// TODO Auto-generated method stub
		return null;
	}

}
