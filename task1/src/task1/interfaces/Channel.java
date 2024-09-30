package task1.interfaces;

import task1.exception.DisconnectedException;

public abstract class Channel {
	protected abstract int read(byte[] bytes, int offset, int length) throws DisconnectedException;
	protected abstract int write(byte[] bytes, int offset, int length) throws DisconnectedException;
	protected abstract void disconnect();
	protected abstract boolean disconnected();
}
