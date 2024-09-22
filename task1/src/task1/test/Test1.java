package task1.test;

import task1.interfaces.Broker;
import task1.interfaces.Channel;
import task1.interfaces.Task;

public class Test1 {
	
	public static void main(String[] args) {
        Broker broker = new BrokerTest("server");
        
        Task t1 = new TaskTest(broker, new Runnable() {
            @Override
            public void run() {
                Channel chan = broker.connect("server", 1111);
                
                byte[] message = "Hello world".getBytes();
                int offset = 0;

                while (offset < message.length) {
                    int bytesWritten = chan.write(message, offset, message.length - offset);
                    offset += bytesWritten;
                }

                byte[] messageArrived = new byte[message.length];
                offset = 0;

                while (offset < message.length) {
                    int bytesRead = chan.read(messageArrived, offset, messageArrived.length - offset);
                    if (bytesRead == -1) break;
                    offset += bytesRead;
                }

                System.out.println("Received from server: " + new String(messageArrived));

                chan.disconnect();
            }
        });

        Task t2 = new TaskTest(broker, new Runnable() {
            @Override
            public void run() {
                Channel chan = broker.accept(1111);
                
                byte[] fullMessage = new byte[100];
                int totalBytesRead = 0;
                int bytesRead;

                while ((bytesRead = chan.read(fullMessage, totalBytesRead, fullMessage.length - totalBytesRead)) != -1) {
                    totalBytesRead += bytesRead;
                    
                    if (totalBytesRead == fullMessage.length) {
                        break;
                    }
                }

                int totalBytesWritten = 0;
                while (totalBytesWritten < totalBytesRead) {
                    totalBytesWritten += chan.write(fullMessage, totalBytesWritten, totalBytesRead - totalBytesWritten);
                }

                // Disconnect the channel
                chan.disconnect();
            }
            
        });

        // Start both tasks
        t2.start();
        t1.start();
    }
	}

