package task1.implementation;

import java.util.concurrent.Semaphore;

public class Rdv {
	public static final int SIZE_CIRCULAR_BUFFER = 100;
	
	BrokerImpl m_brokerA;
	BrokerImpl m_brokerB;
	int m_port; 
	Semaphore waiting;
	ChannelImpl chanForA;
	ChannelImpl chanForB;
	
	public Rdv(BrokerImpl br, int port) {
		m_brokerA = br;
		m_port = port;
		waiting = new Semaphore(1);
		try {
			waiting.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ChannelImpl accept() {
		try {
			waiting.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return chanForA;
	}
	
	public ChannelImpl connect(BrokerImpl br) {
		m_brokerB = br;
		CircularBuffer c1 = new CircularBuffer(SIZE_CIRCULAR_BUFFER);
		CircularBuffer c2 = new CircularBuffer(SIZE_CIRCULAR_BUFFER);
		chanForA = new ChannelImpl(c1, c2);
		waiting.release();
		chanForB = new ChannelImpl(c2, c1);	
		chanForA.setRemoteChannel(chanForB);
		chanForB.setRemoteChannel(chanForA);
		return chanForB;
	}
}
