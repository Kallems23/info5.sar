package task2.test;

import java.util.concurrent.TimeoutException;

import task1.exception.DisconnectedException;
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
				MessageQueue mq = null;
				try {
					mq = TaskImpl.getTask().getQueueBroker().connect("server", 8888);
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				System.out.println("Client connected");
				byte[] message = texttosend.getBytes();

				System.out.println("Client try to send");
				try {
					mq.send(message, 0, message.length);

					System.out.println("Client sended");

					System.out.println("Client try to receive");
					byte[] mreceveived = mq.receive();
					System.out.println("Client received :" + new String(mreceveived));

				} catch (DisconnectedException e) {
					e.printStackTrace();
				}

				mq.close();
			}
		});

		Task t2 = new TaskImpl(B2, new Runnable() {

			@Override
			public void run() {
				System.out.println("Server try to connect");
				MessageQueue mq = TaskImpl.getTask().getQueueBroker().accept(8888);
				System.out.println("Server connected");

				System.out.println("Server try to receive");
				byte[] mreceveived;
				try {
					mreceveived = mq.receive();
					System.out.println("Server received :" + new String(mreceveived));

					System.out.println("Server try to send");
					mq.send(mreceveived, 0, mreceveived.length);
				} catch (DisconnectedException e) {
					e.printStackTrace();
				}
				System.out.println("Server sended");

				mq.close();
			}
		});

		System.out.println("end init");
	}
}
