package task3.implementation;

import java.nio.ByteBuffer;

import task1.exception.DisconnectedException;
import task1.implementation.TaskImpl;
import task1.interfaces.Channel;
import task3.interfaces.MessageQueue;

public class MessageQueueImpl extends MessageQueue {
	Channel m_chan;
	Listener m_l;
	Thread listen;

	public MessageQueueImpl(Channel chan) {
		m_chan = chan;
		listen = new Thread(new Runnable() {

			@Override
			public void run() {
				Task receivedtask = new Task();
				while (!closed()) {
					try {
						byte[] blength = new byte[4];
						int byteReaded = 0;
						while (byteReaded < 4) {

							byteReaded += m_chan.read(blength, byteReaded, 4 - byteReaded);

						}
						int length = ByteBuffer.wrap(blength).getInt();
						byteReaded = 0;
						byte[] bmessage = new byte[length];
						while (byteReaded < length) {
							byteReaded += m_chan.read(bmessage, byteReaded, length - byteReaded);
						}
						receivedtask.post(new Runnable() {

							@Override
							public void run() {
								m_l.received(bmessage);
							}
						});

					} catch (DisconnectedException e) {
					}
				}
			}
		});

		listen.start();
	}

	@Override
	public void setListener(Listener l) {
		m_l = l;
	}

	@Override
	public boolean send(byte[] bytes) {
		return send(bytes, 0, bytes.length);
	}

	@Override
	public boolean send(byte[] bytes, int offset, int length) {
		Task sendTask = new Task();

		sendTask.post(new Runnable() {

			@Override
			public void run() {
				TaskImpl t = new TaskImpl(null, new Runnable() {

					@Override
					public void run() {
						int byteWrited = 0;
						byte[] mlength = ByteBuffer.allocate(4).putInt(length).array();
						while (byteWrited < 4) {
							try {
								byteWrited += m_chan.write(mlength, byteWrited, 4 - byteWrited);
							} catch (DisconnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						byteWrited = offset;
						while (byteWrited < bytes.length + offset) {
							try {
								byteWrited += m_chan.write(bytes, byteWrited + offset, length + offset - byteWrited);
							} catch (DisconnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
			}

		});
		return true;
	}

	@Override
	public void close() {
		listen.interrupt();
	}

	@Override
	public boolean closed() {
		return m_chan.disconnected();
	}

}
