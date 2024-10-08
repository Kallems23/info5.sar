package task1.interfaces;

import java.util.concurrent.TimeoutException;

public abstract class Broker {
	public Broker(String name) {};
	public abstract Channel accept(int port);
	public abstract Channel connect(String name, int port) throws TimeoutException;
	public abstract String name();
}
