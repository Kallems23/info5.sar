package task2.test;

import task1.implementation.BrokerImpl;
import task2.implementation.MessageQueueImpl;
import task2.implementation.QueueBrokerImpl;
import task2.implementation.TaskImpl;
import task2.interfaces.MessageQueue;
import task2.interfaces.QueueBroker;
import task2.interfaces.Task;

public class Test1 {

	public static final String texttosend = "Hello world";

	public static void main(String[] args) {
		QueueBroker B1 = new QueueBrokerImpl(new BrokerImpl("client"));
		QueueBroker B2 = new QueueBrokerImpl(new BrokerImpl("server"));

		// echo test
		Task t1 = new TaskImpl(B1, new Runnable() {

			@Override
			public void run() {
				System.out.println("Client try to connect");
				MessageQueue mq = B1.connect("server", 8888);
				System.out.println("Client connected");
				byte[] message = texttosend.getBytes();

				System.out.println("Client try to send");
				mq.send(message, 0, message.length);
				System.out.println("Client sended");

				System.out.println("Client try to receive");
				byte[] mreceveived = mq.receive();
				System.out.println("Client received :" + mreceveived.toString());

				mq.close();
			}
		});

		Task t2 = new TaskImpl(B2, new Runnable() {

			@Override
			public void run() {
				System.out.println("Server try to connect");
				MessageQueue mq = B2.accept(8888);
				System.out.println("Server connected");

				System.out.println("Server try to receive");
				byte[] mreceveived = mq.receive();
				System.out.println("Server received :" + mreceveived.toString());

				System.out.println("Server try to send");
				mq.send(mreceveived, 0, mreceveived.length);
				System.out.println("Server sended");

				mq.close();
			}
		});
		
		t1.start();
		t2.start();
	}
}
