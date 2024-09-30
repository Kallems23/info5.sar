# Overview: Message Queues

A message queue is a framework to send and receive messages. A message is sent and received as a whole. A message has a variable-sized payload of bytes

# QueuesBroker

A queue broker is a broker that can accept or connect to message queues. The method "accept" will block until a message queue is available on the given port. The method "connect" will block until a message queue is available on the given name and port. 

# MessageQueue

A message queue is a message queue, a point-to-point communication channel. It is a full-duplex communication channel, but the full-duplex nature is not exposed to the user. The user can only send or receive messages.

# Connecting, Writing, Reading, Disconnecting

Same than for channels, but with messages instead of bytes.