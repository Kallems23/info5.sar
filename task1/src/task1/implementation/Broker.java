package task1.implementation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import task1.implementation.Channel;

public class Broker {
	private static BrokerManager brokerManager;
	ConcurrentHashMap<Integer,Rdv> m_rdvlist;
	public Semaphore m_waitingList;
	
	public Broker(String name) {
		m_waitingList = new Semaphore(1);
		m_rdvlist = new ConcurrentHashMap<Integer, Rdv>();
		// Ajout dans le buffer manager
		if (brokerManager == null)
			brokerManager = new BrokerManager();
		brokerManager.addBroker(name, this);
	}

	public Channel accept(int port) {
		Rdv rdv = new Rdv(this, port);
		m_rdvlist.put(port, rdv);
		return rdv.accept();
	}

	public Channel connect(String name, int port) {
		Broker brDistant = brokerManager.findByName(name);
		if(brDistant == null)
			return null;
		while(true){
			try {
				brDistant.m_waitingList.acquire();
			
			if(m_rdvlist.containsKey(port)) {
				Rdv rdv = m_rdvlist.get(port);
				m_rdvlist.remove(port);
				brDistant.m_waitingList.release();
				return rdv.connect(this);
			}
			brDistant.m_waitingList.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
