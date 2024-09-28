package task1.test;

import task1.exception.DisconnectedException;
import task1.implementation.BrokerImpl;
import task1.implementation.ChannelImpl;
import task1.implementation.TaskImpl;

public class Test2 {

	public static final String texttosend = "Hello world";

	public static void main(String[] args) {
		BrokerImpl brokerA = new BrokerImpl("server");
		BrokerImpl brokerB = new BrokerImpl("client");

		System.out.println("begin");

		for (int i = 0; i < 50; i++) {
			String taskname = "t" + i;
			TaskImpl t1 = new TaskImpl(brokerB, new Runnable() {
				@Override
				public void run() {
					System.out.println(taskname+" start");
					ChannelImpl chan = (ChannelImpl) brokerB.connect("server", 1111);
					System.out.println(taskname+" connect");
					byte[] message = texttosend.getBytes();
					int offset = 0;

					while (offset < message.length) {
						int bytesWritten = 0;
						try {
							bytesWritten = chan.write(message, offset, message.length - offset);
						} catch (DisconnectedException e) {
							break;
						}
						offset += bytesWritten;
					}
					System.out.println(taskname+" send");

					byte[] messageArrived = new byte[message.length];
					offset = 0;

					while (offset < message.length) {
						int bytesRead = 0;
						try {
							bytesRead = chan.read(messageArrived, offset, messageArrived.length - offset);
						} catch (DisconnectedException e) {
							break;
						}
						if (bytesRead == -1)
							break;
						offset += bytesRead;
					}

					System.out.println(taskname+" Received from server: " + new String(messageArrived));

					chan.disconnect();
				}
			});
		}

		TaskImpl t2 = new TaskImpl(brokerB, new Runnable() {
			@Override
			public void run() {
				System.out.println("serv start");
				while (true) {
					ChannelImpl chan = (ChannelImpl) brokerA.accept(1111);
					System.out.println("serv connect");
					byte[] fullMessage = new byte[texttosend.length()];
					int totalBytesRead = 0;
					int bytesRead = 0;

					while (totalBytesRead < texttosend.length()) {
						try {
							bytesRead = chan.read(fullMessage, totalBytesRead, texttosend.length() - totalBytesRead);
						} catch (DisconnectedException e) {
							break;
						}
						totalBytesRead += bytesRead;
					}
					System.out.println("serv read" + new String(fullMessage));
					int totalBytesWritten = 0;
					while (totalBytesWritten < totalBytesRead) {
						try {
							totalBytesWritten += chan.write(fullMessage, totalBytesWritten,
									totalBytesRead - totalBytesWritten);
						} catch (DisconnectedException e) {
							break;
						}
					}
					System.out.println("serv sendback");

					// Disconnect the channel
					chan.disconnect();
					System.out.println("serv disconnect");
				}
			}

		});

		System.out.println("end");
	}
}
