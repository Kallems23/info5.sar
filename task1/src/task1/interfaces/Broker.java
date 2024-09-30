package task1.interfaces;

public abstract class Broker {
	protected Broker(String name) {};
	protected abstract Channel accept(int port);
	protected abstract Channel connect(String name, int port);
}
