package task1.implementation;

import java.util.concurrent.ConcurrentHashMap;

public class BrokerManager {
	ConcurrentHashMap<String, Broker> m_list;
	
	public BrokerManager() {
		m_list = new ConcurrentHashMap<String, Broker>();
	}
	
	public void addBroker(String name, Broker br) {
		//if(findByName(name) != null)
			//throw new Exception("name already use");
		m_list.put(name, br);
	}
	
	public Broker findByName(String name) {
		return m_list.get(name);
	}
}
