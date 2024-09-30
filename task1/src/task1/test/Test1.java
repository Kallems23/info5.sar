package task1.test;

import task1.exception.DisconnectedException;
import task1.implementation.BrokerImpl;
import task1.implementation.ChannelImpl;
import task1.implementation.TaskImpl;

public class Test1 {
	
	public static final String texttosend = "Hello world";
	
	public static void main(String[] args) {
        BrokerImpl brokerA = new BrokerImpl("server");
        BrokerImpl brokerB = new BrokerImpl("client");
        

        System.out.println("begin");
        
        TaskImpl t1 = new TaskImpl(brokerB, new Runnable() {
            @Override
            public void run() {
            	System.out.println("t1 start");
                ChannelImpl chan = (ChannelImpl) brokerB.connect("server", 1111);
                System.out.println("t1 connect");
                byte[] message = texttosend.getBytes();
                int offset = 0;

                while (offset < message.length) {
                    int bytesWritten;
					try {
						bytesWritten = chan.write(message, offset, message.length - offset);
					} catch (DisconnectedException e) {
						break;
					}
                    offset += bytesWritten;
                }

                byte[] messageArrived = new byte[message.length];
                offset = 0;

                while (offset < message.length) {
                    int bytesRead = 0;
					try {
						bytesRead = chan.read(messageArrived, offset, messageArrived.length - offset);
					} catch (DisconnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if (bytesRead == -1) break;
                    offset += bytesRead;
                }

                System.out.println("Received from server: " + new String(messageArrived));

                chan.disconnect();
            }
        });

        TaskImpl t2 = new TaskImpl(brokerB, new Runnable() {
            @Override
            public void run() {
            	System.out.println("t2 start");
                ChannelImpl chan = (ChannelImpl) brokerA.accept(1111);
                System.out.println("t2 connect");
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

                int totalBytesWritten = 0;
                while (totalBytesWritten < totalBytesRead) {
                    try {
						totalBytesWritten += chan.write(fullMessage, totalBytesWritten, totalBytesRead - totalBytesWritten);
					} catch (DisconnectedException e) {
						break;
					}
                }

                // Disconnect the channel
                chan.disconnect();
            }
            
        });

        System.out.println("end");
    }
	}

