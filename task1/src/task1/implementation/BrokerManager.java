package task1.implementation;

import java.util.HashMap;

public class BrokerManager {
	HashMap<String, BrokerImpl> m_list;
	
	public BrokerManager() {
		m_list = new HashMap<String, BrokerImpl>();
	}
	
	public synchronized void addBroker(String name, BrokerImpl br) {
		if(get(name) != null)
			throw new IllegalStateException("name already use");
		m_list.put(name, br);
	}
	
	public synchronized BrokerImpl get(String name) {
		return m_list.get(name);
	}
	
	public synchronized void remove(BrokerImpl br) {
		String name = br.getName();
		m_list.remove(name);
	}
}
