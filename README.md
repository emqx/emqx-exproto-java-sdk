# Exproto Java SDK



## Requirements

- JDK 1.8+
- Depend on `emqx-exproto-java-sdk.jar` 

## Get Started

1. First of all, create your Java project.
2. Download the [emqx-exproto-java-sdk.jar](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/SDK/emqx-exproto-java-sdk-0.1.0.jar)  
3. Add sdk: `emqx-exproto-java-sdk.jar` to your project dependency.
4. Copy `example/ExProtoHandlerDemo.java` into your project.
5. Compiled your project and edit exproto plugin properties file, path ```emqx/etc/plugins/emqx_exproto.conf```  
```protperties
exproto.listener.protoname = tcp://0.0.0.0:7993
exproto.listener.protoname.driver = java
exproto.listener.protoname.driver_search_path = data/javaexprotodemo
exproto.listener.protoname.driver_callback_module = ExProtoHandlerDemo
```
6. Copy `ExProtoHandlerDemo.class` and `emqx-exproto-java-sdk.jar` to `emqx/data/javaexprotodemo`
## Java code example
```java
import io.emqx.exproto.*;

/**
 * EMQ X ExProto java SDK;
 * <p>
 * Connection java project to EMQ X Broker.
 * Note 1:
 * Not use "System.in.*" or "System.out.*"  They are used to communicate with EMQ X.
 * Note 2:
 * Invoke  "ExProtoSDK.loadExProtoHandler(AbstractExProtoHandler handler)"
 * or
 * "AbstractExProtoHandler.loadExProtoHandler(AbstractExProtoHandler handler)"
 * <p>
 * load your AbstractExProtoHandler in the "Nonparametric construction method".
 */
public class ExProtoHandlerDemo extends AbstractExProtoHandler {

    public ExProtoHandlerDemo() {
        ExProto.loadExProtoHandler(new ExProtoHandlerDemo(new String[]{"Don't use System.in.*", "Don't use System.out.*"}));
    }

    public ExProtoHandlerDemo(String[] args) {
        for (String arg : args) {
            System.err.println(arg);
        }
    }

    /**
     * call back function :
     * Client connect to EMQ X broker.
     *
     * @param connection     Connection Pid (from erlang data type).
     * @param connectionInfo Client information ; include socket type,socket name,peer name,peer cert.
     */
    @Override
    public void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo) {
        System.err.println(connection);
        System.err.println(connectionInfo);
    }

    /**
     * call back function:
     * Message data from client.
     *
     * @param connection Connection Pid (from erlang data type).
     * @param data       byte arr.
     */
    @Override
    public void onConnectionReceived(Connection connection, byte[] data) {
        System.err.println(connection);
        System.err.println(new String(data));
    }

    /**
     * call back function:
     * Client connection terminated .
     *
     * @param connection Connection Pid (from erlang data type).
     * @param reason     String bytes;
     */
    @Override
    public void onConnectionTerminated(Connection connection, byte[] reason) {
        System.err.println(connection);
        System.err.println(new String(reason));
    }

    /**
     * call back function:
     * Receive message from subscribed topic.
     *
     * @param connection  Connection Pid (from erlang data type).
     * @param messagesArr String bytes;
     */
    @Override
    public void onConnectionDeliver(Connection connection, DeliverMessage[] messagesArr) {
        System.err.println(connection);
        for (DeliverMessage deliverMessage : messagesArr) {
            System.err.println(deliverMessage);
        }
    }
}

```
## SDK Function
### 1. send
```java
    /**
     * SDK provide function:
     * Send message to client connection
     *
     * @param connection Connection Pid (from erlang data type).
     * @param data       Your message data byte arr.
     */
    public void send(Connection connection, byte[] data) throws Exception {
        Erlang.call("emqx_exproto", "send", new Object[]{connection.getPid(), new Binary(data)}, 5000);
    }
```
###  2.terminate
```java
    /**
     * SDK provide function:
     * Terminate client connection.
     *
     * @param connection Connection Pid (from erlang data type).
     */
    public void terminate(Connection connection) throws Exception {
        Erlang.call("emqx_exproto", "close", new Object[]{connection.getPid()}, 5000);
    }

```
###  3. register

```java
    /**
     * SDK provide function:
     * Register a client in EMQ X Broker.
     *
     * @param connection Connection Pid (from erlang data type).
     * @param clientInfo client information, include protocol name, protocol version ,client Id,username,mount point,keep alive time.
     */
    public void register(Connection connection, ClientInfo clientInfo) throws Exception {
        Erlang.call("emqx_exproto", "register", new Object[]{connection.getPid(), ClientInfo.toErlangDataType(clientInfo)}, 5000);
    }
```

### 4.publish
```java
    /**
     * SDK provide function:
     * Publish message to EMQ X Broker
     *
     * @param connection Connection Pid (from erlang data type).
     * @param message    Message information,include message id,qos,from,topic,payload,timestamp.
     */
    public void publish(Connection connection, DeliverMessage message) throws Exception {
        Erlang.call("emqx_exproto", "publish", new Object[]{connection.getPid(), DeliverMessage.toErlangDataType(message)}, 5000);
    }
```
### 5. subscribe
```java
  /**
     * SDK provide function:
     * Subscribe topic in EMQ X Broker
     *
     * @param connection Connection Pid (from erlang data type).
     * @param topic      Topic name.
     * @param qos        qos.
     */
    public void subscribe(Connection connection, String topic, int qos) throws Exception {
        Erlang.call("emqx_exproto", "subscribe", new Object[]{connection.getPid(), new Binary(topic), qos}, 5000);
    }
```
## Note: 
1. NOT read/write `System.out.*` and `System.in` Stream. They are used to communicate with EMQ X.
2. Invoke ``ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)`` load your AbstractExprotoHandler in the ``Nonparametric construction method``.  


## Deploy
1. Execute `bin/emqx console` to start EMQ X and load the `emqx_exproto` plugin.
2. Try to establish a telnet connection and observe the console output.


## Author

- [DDDHuang](https://github.com/DDDHuang)
