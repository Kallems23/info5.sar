package task2.implementation;

import java.nio.ByteBuffer;

import task1.exception.DisconnectedException;
import task1.interfaces.Channel;
import task2.interfaces.MessageQueue;

public class MessageQueueImpl extends MessageQueue {
	Channel m_chan;

	public MessageQueueImpl(Channel channel) {
		m_chan = channel;
	}
	
	/**
	 * send at first the size of the message (a int = 4 first byte sended)
	 */
	@Override
	public void send(byte[] bytes, int offset, int length) throws DisconnectedException {
		int byteWrited = 0;
		byte[] mlength = ByteBuffer.allocate(4).putInt(length).array();
		while(byteWrited < 4) {
			byteWrited += m_chan.write(mlength, byteWrited, 4 - byteWrited);
		}
		
		byteWrited = 0;
		while(byteWrited < length) {
			byteWrited += m_chan.write(bytes, byteWrited, length - byteWrited);
		}
	}

	@Override
	public byte[] receive() throws DisconnectedException {
		byte[] blength = new byte[4];
		int byteReaded = 0;
		while(byteReaded < 4) {
			byteReaded += m_chan.read(blength, byteReaded, 4 - byteReaded);
		}
		int length = ByteBuffer.wrap(blength).getInt();
		byteReaded = 0;
		byte[] bmessage = new byte[length];
		while(byteReaded < length) {
			byteReaded += m_chan.read(bmessage, byteReaded, length - byteReaded);
		}
		return bmessage;
	}

	@Override
	public void close() {
		m_chan.disconnect();
	}

	@Override
	public boolean closed() {
		return m_chan.disconnected();
	}

}
