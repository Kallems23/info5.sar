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
 * One task by broker and port
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
     * litteraly wait a connect
     * 
     * @param name the name of the server
     * @param port the port use to connect 2 entities
     * @return a channel between the server and the client
     */
    Channel connect(String name, int port);
    /**
     * Create a channel between the server and the client
     * 
     * litteraly wait an accept (with a timeout)
     * 
     * @param port the port use to connect 2 entities
     * @return a channel between the server and the client
     *         null if the named broker is not found
     */
}

/**
 * A channel is a connection between 2 tasks, a point-to-point stream of bytes, where we can read and write from each side (a task as only one channel)
 * 
 * FIFO + lossless
 * 
 * Main use is to connect 2 tasks
 * 
 * Thread safe for read and write
 * - 1 read and 1 write at the same time on each side
 * - 1 read and 1 write at the same time on the same side
 * 
 * To mark the end of a stream, the corresponding channel is simply disconnected.
 *
 *
 * He was created by a broker
 */
abstract class Channel {
    int read(byte[] bytes, int offset, int length); 
    /**
     * Read the bytes from the channel
     * 
     * example of use :
     *    void receive(byte[] bytes) throws DisconnectedException {
     *      int remaining = bytes.length;
     *      int offset = 0;
     *      while (remaining!=0) {
     *          int n = channel.read(bytes,offset,remaining);
     *          offset += n;
     *          remaining -= n;
     *      }
     *    }
     * 
     * The end of stream is the same as being as the channel being disconnected, 
     * so the method will throw an exception (DisconnectedException). 
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
     * example of use :
     *   void send(byte[] bytes) {
     *      int remaining = bytes.length;
     *      int offset = 0;
     *      while (remaining!=0) {
     *           int n = channel.write(bytes,offset,remaining);
     *           offset += n;
     *           remaining -= n;
     *      }
     *   }
     * 
     * @param bytes the bytes to write
     * @param offset the offset of the bytes tab
     * @param length the length of writing
     * @return the number of bytes written
     */

    void disconnect();
    /**
     * Disconnect the channel
     * 
     * When either side of a channel disconnects, it has to be handled asynchronously. 
     * 
     * Locally, once the disconnect method is called, it’s no longer allowed to use the read or write methods, and only the disconnected method can be used to check the status. 
     * If read or write is called after disconnection, an exception is thrown.
     * 
     * If the remote side disconnects, the local side should still be able to read any remaining bytes that were sent before the disconnection. 
     * The local side is only considered disconnected after all those bytes are read or if the channel is locally disconnected. 
     * 
     * In this half-disconnected state, any attempt to write locally will result in the bytes being silently dropped. 
     * This ensures that the final message is received before disconnection and avoids issues that arise from immediate, synchronous disconnections.
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