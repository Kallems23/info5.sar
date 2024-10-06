package task3.test;

import task3.implementation.QueueBrokerImpl;
import task3.interfaces.MessageQueue;
import task3.interfaces.MessageQueue.Listener;
import task3.interfaces.QueueBroker;
import task3.interfaces.QueueBroker.AcceptListener;
import task3.interfaces.QueueBroker.ConnectListener;

public class Test1 {
	public static final String texttosend = "Hello world";

	public static void main(String[] args) {
		QueueBroker QA = new QueueBrokerImpl("server");
		QueueBroker QB = new QueueBrokerImpl("client");
		QueueBroker QC = new QueueBrokerImpl("client2");

		// Echo server
		QA.bind(8080, new AcceptListener() {

			@Override
			public void accepted(MessageQueue queue) {
				System.out.println("Server : Connection accepted");
				queue.setListener(new Listener() {

					@Override
					public void received(byte[] msg) {
						queue.send(msg);
						queue.close();
					}

					@Override
					public void closed() {
						System.out.println("Server : Connection closed");
					}
				});
			}
		});

		// Classic client
		QB.connect("server", 8080, new ConnectListener() {

			@Override
			public void refused() {
				System.out.println("Client : Connection refused");
			}

			@Override
			public void connected(MessageQueue queue) {
				System.out.println("Client : Connection connected");
				queue.setListener(new Listener() {

					@Override
					public void received(byte[] msg) {
						System.out.println("Client : Received from Server (" + new String(msg) + ")");
						queue.close();
					}

					@Override
					public void closed() {
						System.out.println("Client : Connection closed");
					}
				});
				queue.send(texttosend.getBytes());

			}
		});

		// Classic client
		QC.connect("server", 8080, new ConnectListener() {

			@Override
			public void refused() {
				System.out.println("Client2 : Connection refused");
			}

			@Override
			public void connected(MessageQueue queue) {
				System.out.println("Client2 : Connection connected");
				queue.setListener(new Listener() {

					@Override
					public void received(byte[] msg) {
						System.out.println("Client2 : Received from Server (" + new String(msg) + ")");
						queue.close();
					}

					@Override
					public void closed() {
						System.out.println("Client2 : Connection closed");
					}
				});
				queue.send(texttosend.getBytes());

			}
		});

	}
}
