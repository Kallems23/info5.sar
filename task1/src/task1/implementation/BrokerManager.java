package task1.implementation;

import java.util.HashMap;

public class BrokerManager {
	HashMap<String, Broker> m_list;
	
	public BrokerManager() {
		m_list = new HashMap<String, Broker>();
	}
	
	public synchronized void addBroker(String name, Broker br) {
		if(get(name) != null)
			throw new IllegalStateException("name already use");
		m_list.put(name, br);
	}
	
	public synchronized Broker get(String name) {
		return m_list.get(name);
	}
	
	public synchronized void remove(Broker br) {
		String name = br.getName();
		m_list.remove(name);
	}
}
