package task1.implementation;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import task1.interfaces.Broker;
import task1.interfaces.Channel;

public class BrokerImpl extends Broker {
	private static BrokerManager brokerManager;
	HashMap<Integer, Rdv> m_rdvlist;
	public Semaphore m_waitingList;
	protected String m_name;

	public BrokerImpl(String name) {
		super(name);
		m_name = name;
		m_waitingList = new Semaphore(1);
		m_rdvlist = new HashMap<Integer, Rdv>();
		// Ajout dans le buffer manager
		if (brokerManager == null)
			brokerManager = new BrokerManager();
		brokerManager.addBroker(name, this);
	}

	public Channel accept(int port) {
		Rdv rdv;
		while (true) {
			try {
				m_waitingList.acquire();
				rdv = new Rdv(this, port);
				m_rdvlist.put(port, rdv);
				m_waitingList.release();
				return rdv.accept();
			} catch (InterruptedException e) {
			}
		}
	}

	public String getName() {
		return m_name;
	}

	public Channel connect(String name, int port) {
		BrokerImpl brDistant = brokerManager.get(name);
		if (brDistant == null)
			return null;
		while (true) {
			try {
				brDistant.m_waitingList.acquire();

				if (brDistant.m_rdvlist.containsKey(port)) {
					Rdv rdv = brDistant.m_rdvlist.get(port);
					brDistant.m_rdvlist.remove(port);
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
