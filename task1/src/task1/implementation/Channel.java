package task1.implementation;

public class Channel {
	CircularBuffer writingBuffer;
	CircularBuffer readingBuffer;
	boolean isDisconnected;
	
	
	public Channel(CircularBuffer wr, CircularBuffer read) {
		writingBuffer = wr;
		readingBuffer = read;
		isDisconnected = false;
	}
	
	public int read(byte[] bytes, int offset, int length) {
		int i = offset;
		while(!readingBuffer.empty() && i < bytes.length) {
			bytes[i] = readingBuffer.pull();
			i++;
		}
		return i-offset;
	}

	public int write(byte[] bytes, int offset, int length) {
		int i = offset;
		while(!writingBuffer.full() && i < bytes.length) {
			writingBuffer.push(bytes[i]);
			i++;
		}
		return i-offset;
	}

	public void disconnect() {
		isDisconnected = true;
	}

	public boolean disconnected() {
		return isDisconnected;
	}

}
