# Exproto Java SDK



## Requirements

- JDK 1.8+
- Depend on `erlport.jar` `emqx-exproto-java-sdk.jar` 

## Get Started

1. First of all, create your Java project.
2. Download the [emqx-exproto-java-sdk.jar](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/SDK/emqx-exproto-java-sdk-0.1.0.jar) and [erlport.jar](https://github.com/emqx/emqx-extension-java-sdk/blob/master/deps/erlport-v1.1.1.jar)
3. Add the sdk: `emqx-exproto-java-sdk.jar` and `erlport.jar` to your project dependency.
4. Write your code :  
 step 1  
 Extends ``io.emqx.exproto.sdk.AbstractExProtoHandler``.  
 step 2  
 Invoke  ``ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)`` load your AbstractExprotoHandler in the ``Nonparametric construction method``.  
 step 3  
 Override abstract method.  
5. Compiled your project and edit exproto plugin properties file, path ```emqx/etc/plugins/emqx_exproto.conf```  
```protperties
exproto.listener.protoname.driver = java
exproto.listener.protoname.driver_search_path = your *.class file path
exproto.listener.protoname.driver_callback_module = your class name (extends AbstractExprotoHandler)
```
6. Copy *.class file and `emqx-exproto-java-sdk.jar` to your specified path.
 ## Code Example
```java
import com.erlport.erlang.term.Pid;
import io.emqx.exproto.sdk.AbstractExProtoHandler;
import io.emqx.exproto.sdk.ConnectionInfo;
import io.emqx.exproto.sdk.DeliverMessage;
import io.emqx.exproto.sdk.ExProtoSDK;
/**
 * EMQ X Exproto java SDK;
 * 
 * Connection java project to EMQ X Broker.
 * Note 1:
 * Not use "System.in.*" or "System.out.*"  They are used to communicate with EMQ X.
 * Note 2:
 * Invoke  "ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)"
 * or
 * "AbstractExprotoHandler.loadExprotoHandler(AbstractExprotoHandler handler)"
 * 
 * SDK provide function:
 * 
 * void sendToConnection(Pid connId, byte[] data)
 * 
 * void terminateConnection(Pid connId)
 * 
 * void registerClient(Pid connId, ClientInfo clientInfo)
 * 
 * void publishMessage(Pid connPid, DeliverMessage message)
 * 
 * void subscribeTopic(Pid connId, String topic, int qos)
 */
public class ExprotoHandlerDemo extends AbstractExProtoHandler {

    /*
       TODO :  build your nonparametric construction method,
               and invoke " ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)" ,load your handler in SDK;
   
               as :
                public ExprotoHandlerDemo() {
                   AbstractExprotoHandler handler =new ExprotoHandlerDemo("ExprotoHandler Name");
                   ExprotoSDK.loadExprotoHandler(handler);
               }
        */
   
       public ExprotoHandlerDemo() {
           AbstractExProtoHandler handler = new ExprotoHandlerDemo("ExprotoHandler Name");
           ExProtoSDK.loadExProtoHandler(handler);
       }
   
       String name;
   
       public ExprotoHandlerDemo(String name) {
           this.name = name;
       }
   
       /**
        * call back function :
        * Client connect to EMQ X broker.
        *
        * @param connId         Connection Pid (from erlang data type).
        * @param connectionInfo Client information ; include socket type,socket name,peer name,peer cert.
        */
       @Override
       public void onConnectionEstablished(Pid connId, ConnectionInfo connectionInfo) {
   
       }
   
       /**
        * call back function:
        * Message data from client.
        *
        * @param connId Connection Pid (from erlang data type).
        * @param data   byte arr.
        */
       @Override
       public void onConnectionReceived(Pid connId, byte[] data) {
   
       }
   
       /**
        * call back function:
        * Client connection terminated .
        *
        * @param connId Connection Pid (from erlang data type).
        * @param reason String bytes;
        */
       @Override
       public void onConnectionTerminated(Pid connId, byte[] reason) {
   
       }
   
       /**
        * call back function:
        * Receive message from subscribed topic.
        *
        * @param connId      Connection Pid (from erlang data type).
        * @param messagesArr String bytes;
        */
       @Override
       public void onConnectionDeliver(Pid connId, DeliverMessage[] messagesArr) {
   
       }
}

```
## SDK Function
### 1. sendToConnection
```java
/**
 * SDK provide function:
 * Send message to client connection
 *
 * @param connId Connection Pid (from erlang data type).
 * @param data   Your message data byte arr.
 */
public void sendToConnection(Pid connId, byte[] data)throws Exception{
    Erlang.call("emqx_exproto", "send", new Object[]{connId, new Binary(data)}, 5000);
}
```
###  2.terminateConnection
```java
/**
 * SDK provide function:
 * Terminate client connection.
 *
 * @param connId Connection Pid (from erlang data type).
 */
public void terminateConnection(Pid connId) throws Exception {
    Erlang.call("emqx_exproto", "close", new Object[]{connId}, 5000);
}
```
###  3. registerClient

```java
 /**
 * SDK provide function:
 * Register a client in EMQ X Broker.
 *
 * @param connId     Connection Pid (from erlang data type).
 * @param clientInfo client information, include protocol name, protocol version ,client Id,username,mount point,keep alive time.
 */
public void registerClient(Pid connId, ClientInfo clientInfo) throws Exception {
    Erlang.call("emqx_exproto", "register", new Object[]{connId, ClientInfo.toErlangDataType(clientInfo)}, 5000);
}
```

### 4.publishMessage
```java
/**
 * SDK provide function:
 * Publish message to EMQ X Broker
 *
 * @param connPid Connection Pid (from erlang data type).
 * @param message Message information,include message id,qos,from,topic,payload,timestamp.
 */
public void publishMessage(Pid connPid, DeliverMessage message) throws Exception {
    Erlang.call("emqx_exproto", "publish", new Object[]{connPid, DeliverMessage.toErlangDataType(message)}, 5000);
}
```
### 5. subscribeTopic
```java
/**
 * SDK provide function:
 * Subscribe topic in EMQ X Broker
 *
 * @param connId Connection Pid (from erlang data type).
 * @param topic  Topic name.
 * @param qos    qos.
 */
public void subscribeTopic(Pid connId, String topic, int qos) throws Exception {
    Erlang.call("emqx_exproto", "subscribe", new Object[]{connId, new Binary(topic), qos}, 5000);
}
```
## Note: 
1. NOT read/write `System.out.*` and `System.in` Stream. They are used to communicate with EMQ X.
2. Invoke ``ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)`` load your AbstractExprotoHandler in the ``Nonparametric construction method``.  


## Deploy
1. Execute `bin/emqx console` to start EMQ X and load the `emqx_exproto` plugin.
2. Try to establish a connection and observe the console output.


## Autor

- [DDDHuang](https://github.com/DDDHuang)
