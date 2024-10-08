package task2.interfaces;

import java.util.concurrent.TimeoutException;

import task1.interfaces.Broker;

public abstract class QueueBroker {
	 public QueueBroker(Broker broker) {}
	 public abstract String name();
	 public abstract MessageQueue accept(int port);
	 public abstract MessageQueue connect(String name, int port) throws TimeoutException;
	}