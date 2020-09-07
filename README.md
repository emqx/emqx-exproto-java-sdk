# ExProto Java SDK

## Requirements

- JDK 1.8+

- Depend on erlport.jar (The communication module used in emqx-exproto-java-sdk)

## SDK edition & EMQ X Broker edition

| SDK edition | EMQ X Broker edition |
| ----------- | -------------------- |
| 0.0.2       | 4.2.0                |
| unsupport   | before 4.2.0         |

## Get Started

1. First of all, create your Java project.

2. See `SDK edition & EMQ X Broker edition` part. Chose your SDK edition.Depends on EMQ X Broker edition.

   Download the [emqx-exproto-java-sdk.jar](https://search.maven.org/search?q=emqx) and [erlport.jar](https://github.com/emqx/emqx-exproto-java-sdk/raw/master/src/lib/erlport.jar).

3. Add `emqx-exproto-java-sdk.jar` and `erlport.jar` to your project dependency.

   If your project is a maven project, add a dependency in your maven project `pom.xml` `<dependencies></dependencies>`.


   ```xml
   <dependency>
     <groupId>io.emqx</groupId>
     <artifactId>emqx-exproto-java-sdk</artifactId>
     <!-- Chose your SDK edition.Depends on EMQ X Boker edition. -->
     <!-- Change version to your chosen SDK edition -->
     <version>version</version>
   </dependency>
   ```
   Change `<version>version</version>` to your chosen SDK edition, like `<version>0.0.1</version>`.

4. Copy `example/ExProtoHandlerDemo.java` into your project.

5. Try to compile your project.

Note: NOT read/write `System.out` and `System.in` stream. They are used to communicate with EMQ X.
Note: Invoke `ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)` load your AbstractExprotoHandler in the `Nonparametric construction method`.

## Deploy

After compiled all source codes, you should deploy the sdk and your class files into EMQ X.

1. Copy the `emqx-exproto-java-sdk.jar` to `emqx/data/extension` directory.

2. Copy your class files, e.g: `ExProtoHandlerDemo.class` to `emqx/data/extension` directory.

3. Modify the `emqx/etc/plugins/emqx_exproto.conf` file. e.g:

    ```protperties
    exproto.listener.protoname = tcp://0.0.0.0:7993
    exproto.listener.protoname.driver = java
    exproto.listener.protoname.driver_search_path = data/extension
    exproto.listener.protoname.driver_callback_module = ExProtoHandlerDemo
    ```
    
4. Execute `bin/emqx console` to start EMQ X and load the `emqx_exproto` plugin.

5. Use `telnet 127.0.0.1 7993` to establish a TCP connection and observe the console output.

## Examples

see: [examples/ExProtoHandlerDemo.java](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/example/ExProtoHandlerDemo.java).

`examples/ExProtoHandlerDemo.java` useage see [java-sdk-quick-guide.md](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/java-sdk-quick-guide.md).

## Interface

The `AbstractExProtoHandler` provide a series of interfaces, that is an encapsulation for `emqx-exproto`.

### Callbacks

**Connection Layer callbacks** (The Connection object represents a TCP/UDP Socket entity):

``` java
// This function will be scheduled after a TCP connection established to EMQ X
//  or receive a new UDP socket.
public abstract void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo);

// This callback will be scheduled when a connection received bytes from TCP/UDP socket.
public abstract void onConnectionReceived(Connection connection, byte[] data);

// This function will be scheduled after a connection terminated.
//
// It indicates that the EMQ X process that maintains the TCP/UDP socket
// has been closed. E.g: a TCP connection is closed, or a UDP socket has
// exceeded maintenance hours.
public abstract void onConnectionTerminated(Connection connection, byte[] reason);
```

**Pub/Sub Layer callbacks:**

``` java
// This function will be scheduled when a connection received a Message from EMQ X
//
// When a connection is subscribed to a topic and a message arrives on that topic,
// EMQ X will deliver the message to that connection. At that time, this function
// is triggered.
public abstract void onConnectionDeliver(Connection connection, DeliverMessage[] messagesArr);
```

### APIs

Similarly, `AbstractExprotoHandler` also provides a set of APIs to facilitate the use of the `emqx-exproto` APIs.


**Connection Layer APIs:**

``` java
// Send a stream of bytes to the connection. These bytes are delivered directly
// to the associated TCP/UDP socket.
public static void send(Connection connection, byte[] data) throws Exception;

// Terminate the connection process and TCP/UDP socket.
public static void terminate(Connection connection) throws Exception;
```

**Pub/Sub Layer APIs:**

```java
// Register the connection as a Client of EMQ X. This `clientInfo` contains the
// necessary field information to be an EMQ X client.
//
// This method should normally be invoked after confirming that a connection is
// allowed to access the EMQ X system. For example: after the connection packet
// has been parsed and authenticated successfully.
public static void register(Connection connection, ClientInfo clientInfo) throws Exception;

// The connection Publish a Message to EMQ X
public static void publish(Connection connection, DeliverMessage message) throws Exception;

// The connection Subscribe a Topic to EMQ X
public static void subscribe(Connection connection, String topic, int qos) throws Exception;
```

## License

Apache License v2

## Author

- [DDDHuang](https://github.com/DDDHuang)
