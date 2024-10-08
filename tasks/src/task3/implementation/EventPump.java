package task3.implementation;

import java.util.LinkedList;

public class EventPump extends Thread {
	public static EventPump ev;

	public static EventPump getPump() {
		if (ev == null) {
			ev = new EventPump();
			ev.start();
		}
		return ev;
	}

	LinkedList<Runnable> queue;

	EventPump() {
		queue = new LinkedList<Runnable>();
	}

	public synchronized void run() {
		Runnable r;
		while (true) {
			r = queue.poll();
			while (r != null) {
				r.run();
				r = queue.poll();
			}
			sleep();
		}
	}

	public synchronized void post(Runnable r) {
		queue.add(r);
		notify();
	}

	private void sleep() {
		try {
			wait();
		} catch (InterruptedException ex) {
			// nothing to do here.
		}
	}
}
