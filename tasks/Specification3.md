# Overview: Message Queues (Event-based)

A message queue stay a framework to send and receive messages. A message is sent and received as a whole. A message has a variable-sized payload of bytes

# QueuesBroker

The connection is now event-based. The method "accept" is now not blocking. 

You may use bind to add a callback to the broker accept.
It will return imediatly if the accept is possible or not.
And will call method "accepted" when is accepte connection.

As same, the method "connect" is now not blocking.
You may use bind to add a callback to the broker connect.

You have to implement the AcceptListener and ConnectListener interface to execute code when the connection is accepted or connected.

# Sending and Receiving

When we send a message, we will call the method send, try to send the message and return false if it not possible to send the message.
Same for reading.


# Example of usage : echo server

```java
public class Echo implements AcceptListener {
    @Override
    public void accepted(MessageQueue queue) {
        byte[] message = queue.receive();
        queue.send(message);
    }
}

public class EchoServer {
    public static void main(String[] args) {
        QueuesBroker broker = new QueuesBroker(new Broker("echoserver"));

        Task acceptTask = new Task(broker, new Runnable() {
            @Override
            public void run() {
                    Task.getBroker().bind((8080, new Echo()));
            }
        });

    }
}

```
