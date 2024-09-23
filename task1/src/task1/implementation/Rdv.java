package task1.implementation;

import java.util.concurrent.Semaphore;

public class Rdv {
	public static final int SIZE_CIRCULAR_BUFFER = 100;
	
	Broker m_brokerA;
	Broker m_brokerB;
	int m_port; 
	Semaphore waiting;
	Channel chanForA;
	
	public Rdv(Broker br, int port) {
		m_brokerA = br;
		m_port = port;
		waiting = new Semaphore(1);
		try {
			waiting.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Channel accept() {
		try {
			waiting.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return chanForA;
	}
	
	public Channel connect(Broker br) {
		m_brokerB = br;
		CircularBuffer c1 = new CircularBuffer(SIZE_CIRCULAR_BUFFER);
		CircularBuffer c2 = new CircularBuffer(SIZE_CIRCULAR_BUFFER);
		chanForA = new Channel(c1, c2);
		waiting.release();
		return new Channel(c2, c1);		
	}
}
