# Specification

## Use case

* The client should be able to connect to the server

* The server should be able to accept the connection from different clients at the same time

* The client should be able to send send a sequence of bytes, representing the number from 1 to 255 to the server

* The serve should be able to receive the sequence of bytes from the client

* The server should be able to send the sequence of bytes back to the client

* The client should be able to disconnect from the server


## Class specification

```java

/**
 * A broker is a tool that can create a channel and a task (tread-safe)
 * 
 * That can be a server or a client
 */
abstract class Broker {
    Broker(String name); 
    /**
     * Create a broker with a name (machine name)
     * 
     * @param name the name of the broker
     */
    Channel accept(int port);
    /**
     * Accept connect from a client
     * 
     * @param name the name of the server
     * @param port the port use to connect 2 entities
     * @return a channel between the server and the client
     */
    Channel connect(String name, int port);
    /**
     * Create a channel between the server and the client
     * 
     * @param port the port use to connect 2 entities
     * @return a channel between the server and the client
     */
}

/**
 * A channel is a connection between 2 tasks (a task as only one channel)
 * 
 * He was created by a broker
 */
abstract class Channel {
    int read(byte[] bytes, int offset, int length); 
    /**
     * Read the bytes from the channel
     * 
     * @param bytes the bytes to stock the data read
     * @param offset the offset of the bytes tab
     * @param length the length of reading
     * @return the number of bytes read
     */

    int write(byte[] bytes, int offset, int length);
    /**
     * Write the bytes to the channel
     * 
     * @param bytes the bytes to write
     * @param offset the offset of the bytes tab
     * @param length the length of writing
     * @return the number of bytes written
     */

    void disconnect();
    /**
     * Disconnect the channel
     */

    boolean disconnected();
    /**
     * Check if the channel is disconnected
     * 
     * @return true if the channel is disconnected
     */
}

/**
 * A task is a thread that can be executed by a client or a server
 * 
 */
abstract class Task extends Thread{
    Task(Broker b, Runnable r);
    /**
     * Create a task with a broker and a runnable
     * 
     * @param b the main broker that execute the task
     * @param r the runnable that the task execute
     */
    static Broker getBroker();
    /**
     * Get the main broker that execute the task
     * 
     * @return the broker that execute the task
     */

}
```