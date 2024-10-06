package task1.implementation;

import task1.exception.DisconnectedException;
import task1.interfaces.Channel;

public class ChannelImpl extends Channel {
	ChannelImpl remoteChan;
	CircularBuffer writingBuffer;
	CircularBuffer readingBuffer;
	boolean isDisconnected;
	boolean remoteisDisconnected;

	public ChannelImpl(CircularBuffer wr, CircularBuffer read) {
		writingBuffer = wr;
		readingBuffer = read;
		isDisconnected = false;
		remoteisDisconnected = false;
	}

	public void setRemoteChannel(ChannelImpl otherchan) {
		remoteChan = otherchan;
	}

	public int read(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (isDisconnected)
			throw new DisconnectedException();
		int i = offset;
		synchronized (readingBuffer) {
			while (!readingBuffer.empty() && i < bytes.length) {
				bytes[i] = readingBuffer.pull();
				i++;
			}
		} if (readingBuffer.empty() && remoteisDisconnected) {
			isDisconnected = true;
		}
		return i - offset;
	}

	public int write(byte[] bytes, int offset, int length) throws DisconnectedException {
		if (isDisconnected)
			throw new DisconnectedException();
		int i = offset;
		synchronized (writingBuffer) {
			while (!writingBuffer.full() && i < bytes.length) {
				writingBuffer.push(bytes[i]);
				i++;
			}
		}
		return i - offset;
	}

	public void disconnect() {
		synchronized (this) {
			if (disconnected())
				return;
			isDisconnected = true;
			remoteChan.remoteisDisconnected = true;
		}

	}

	public boolean disconnected() {
		return isDisconnected;
	}

}
