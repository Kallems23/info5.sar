package task1.test;

import task1.implementation.Broker;
import task1.implementation.Channel;
import task1.implementation.Task;

public class Test20 {

	public static void main(String[] args) {
		Broker brokerA = new Broker("server");
		Broker brokerB = new Broker("client");

		System.out.println("begin");

		for (int i = 0; i < 2; i++) {
			String taskname = "t" + i;
			Task t1 = new Task(brokerB, new Runnable() {
				@Override
				public void run() {
					System.out.println(taskname + " start");
					Channel chan = brokerB.connect("server", 1111);
					System.out.println(taskname + " connect");
					String messagetosend = taskname + " Hello world";
					byte[] message = messagetosend.getBytes();
					int offset = 0;

					while (offset < message.length) {
						int bytesWritten = chan.write(message, offset, message.length - offset);
						offset += bytesWritten;
					}

					byte[] messageArrived = new byte[message.length];
					offset = 0;

					while (offset < message.length) {
						int bytesRead = chan.read(messageArrived, offset, messageArrived.length - offset);
						if (bytesRead == -1)
							break;
						offset += bytesRead;
					}

					System.out.println(taskname + " : Received from server: " + new String(messageArrived));

					chan.disconnect();
				}
			});
		}

		Task t2 = new Task(brokerB, new Runnable() {
			@Override
			public void run() {
				System.out.println("serv start");
				while (true) {
					Channel chan = brokerA.accept(1111);
					System.out.println("serv connect");
					byte[] fullMessage = new byte[100];
					int totalBytesRead = 0;
					int bytesRead;

					while ((bytesRead = chan.read(fullMessage, totalBytesRead,
							fullMessage.length - totalBytesRead)) != -1) {
						totalBytesRead += bytesRead;

						if (totalBytesRead == fullMessage.length) {
							break;
						}
					}

					int totalBytesWritten = 0;
					while (totalBytesWritten < totalBytesRead) {
						totalBytesWritten += chan.write(fullMessage, totalBytesWritten,
								totalBytesRead - totalBytesWritten);
					}

					// Disconnect the channel
					chan.disconnect();
				}
			}

		});

		System.out.println("end");
	}
}
