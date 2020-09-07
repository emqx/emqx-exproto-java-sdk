# 	Java SDK 快速入门

我们提供了 Java 语言适用的 SDK，以帮助快速、方便地开发 Java 语言的 EMQ X 扩展。

## 准备

1. 建议 JDK 1.8 及以上。

2. 在 IDE 中创建 Java 项目，将 `emqx-exproto-java-sdk.jar` 和`erlport.jar`作为依赖引入该项目。

## 下载依赖

### SDK `emqx-exproto-java-sdk.jar`

驱动版本选择参考[README.md](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/README.md)   ``SDK edition & EMQ X Broker edition``章节。 

#### 1 直接下载jar

进入[下载页面](https://search.maven.org/search?q=emqx)，选择与您正在使用的EMQ X Broker版本对应的驱动。

#### 2 使用maven引入`emqx-exproto-java-sdk.jar`

打开maven项目的pom.xml文件，在`<dependencies></dependencies>`标签内，添加记录：

```xml
<dependency>
  <groupId>io.emqx</groupId>
  <artifactId>emqx-exproto-java-sdk</artifactId>
  <!-- 
			版本选择请按照部署的EMQ X Broker版本选择，版本对应请参考
			https://github.com/emqx/emqx-exproto-java-sdk/blob/master/README.md 
			的 SDK edition & EMQ X Broker edition 章节，
			此处的版本号<version>0.0.1</version>仅作为示例
 -->
  <version>0.0.1</version>
</dependency>
```

### SDK 依赖`erlport.jar`

使用[下载链接](https://github.com/emqx/emqx-exproto-java-sdk/raw/master/src/lib/erlport.jar)下载。

## 示例

我们提供了 [ExProtoHandlerDemo.java](https://github.com/emqx/emqx-exproto-java-sdk/blob/master/example/ExProtoHandlerDemo.java) 示例程序，此示例程序提供了终端与EMQ X 插件的简单的交互功能。

该程序继承自 SDK 中的 `AbstractExProtoHandler` 类。

```java
import io.emqx.exproto.*;

import java.util.Arrays;

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


    private static ExProtoHandlerDemo exProtoHandlerDemo = new ExProtoHandlerDemo(new String[]{"don't use [System.in.*]  or [System.out.*]"});

    public ExProtoHandlerDemo() {
        ExProto.loadExProtoHandler(exProtoHandlerDemo);
    }

    public ExProtoHandlerDemo(String[] args) {
        for (String arg : args) {
            System.err.println(arg);
        }
    }

    String help =
            "hello      -->     say hello to AbstractExProtoHandler\r\n" +
                    "close      -->     close conn\r\n" +
                    "reg        -->     register client \r\n" +
                    "pub        -->     publish message\r\n" +
                    "sub        -->     subscribe mytopic qos 1\r\n" +
                    "unsub      -->     unsubscribe mytopic";

    /**
     * A connection established.
     * <p>
     * This function will be scheduled after a TCP connection established to EMQ X
     * or receive a new UDP socket.
     *
     * @param connection     The Connection instance
     * @param connectionInfo The Connection information
     */
    @Override
    public void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo) {
        System.err.println("onConnectionEstablished " + connection.getPid() + "  " + connectionInfo);
        try {
            send(connection, help.getBytes());
        } catch (Exception e) {
        }
    }

    /**
     * A connection received bytes.
     * <p>
     * This callback will be scheduled when a connection received bytes from TCP/UDP socket.
     *
     * @param connection The Connection instance
     * @param data       The bytes array
     */
    @Override
    public void onConnectionReceived(Connection connection, byte[] data) {
        String command = new String(data).trim();
        System.err.println(command);
        switch (command) {
            case "":
                break;
            case "hello":
                try {
                    send(connection, ("hello my friend " + connection.getPid().id + "\r\n").getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "close":
                try {
                    send(connection, ("goodbye my friend " + connection.getPid().id + "\r\n").getBytes());
                    terminate(connection);
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "reg":
                try {
                    ClientInfo clientInfo = new ClientInfo("mqtt", "3.1", "testCID", "testUname", "testMP/", 300);
                    register(connection, clientInfo);
                    System.err.println(ClientInfo.toErlangDataType(clientInfo));
                    send(connection, ("register a client  " + clientInfo.toString()).getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "pub":
                try {
                    Message message =
                            new Message("testId", 0, "from", "mytopic", "pubmessage".getBytes(), System.currentTimeMillis());
                    publish(connection, message);
                    send(connection, ("publish " + message.toString()).getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "sub":
                try {
                    String topic = "mytopic";
                    subscribe(connection, topic, 1);
                    System.err.println("subscribe " + topic);
                    send(connection, ("subscribe " + topic + " qos " + 1).getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "unsub":
                try {
                    String unSubTop = "mytopic";
                    unsubscribe(connection, unSubTop);
                    System.err.println("subscribe " + unSubTop);
                    send(connection, ("unsubscribe " + unSubTop).getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "help":
                try {
                    send(connection, help.getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            default:
                try {
                    send(connection, ("i don't know " + command + "\r\n").getBytes());
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
        }

    }

    /**
     * A connection terminated.
     * <p>
     * This function will be scheduled after a connection terminated.
     * <p>
     * It indicates that the EMQ X process that maintains the TCP/UDP socket
     * has been closed. E.g: a TCP connection is closed, or a UDP socket has
     * exceeded maintenance hours.
     *
     * @param connection The Connection instance
     * @param reason     The Connection terminated reason
     */
    @Override
    public void onConnectionTerminated(Connection connection, String reason) {
        System.err.println("onConnectionTerminated " + connection.getPid() + " Reason " + reason);
    }

    /**
     * A connection received a serial of messages from subscribed topic.
     * <p>
     * This function will be scheduled when a connection received a Message from EMQ X
     * <p>
     * When a connection is subscribed to a topic and a message arrives on that topic,
     * EMQ X will deliver the message to that connection. At that time, this function
     * is triggered.
     *
     * @param connection  The Connection instance
     * @param messagesArr The message array
     */
    @Override
    public void onConnectionDeliver(Connection connection, Message[] messagesArr) {
        System.err.println("onConnectionDeliver " + connection.getPid() + "  " + Arrays.toString(messagesArr));
        try {
            send(connection, ("\nonConnectionDeliver " + Arrays.toString(messagesArr)).getBytes());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
```
示例中实现了4个回调函数，分别处理以下事件：
| 函数                    | 回调事件                   |
| ----------------------- | -------------------------- |
| onConnectionEstablished | 终端建立连接               |
| onConnectionReceived    | 终端发送消息至EMQ X Borker |
| onConnectionTerminated  | 终端断开连接               |
| onConnectionDeliver     | EMQ X Broker订阅消息       |

### 示例程序使用方式

1 启动EMQ X Broker

```shell
./bin/emqx console
```

2 使用telnet程序接入EMQ X Broker

```shell
#[IP] 	: EMQ X Broker部署的IP
#[Port]	: SDK配置（详见部署章节）中指定的监听端口
telnet [IP] [Port]
```

3 使用一些简单交互命令

| 指令  | 操作                                |
| ----- | ----------------------------------- |
| hello | say hello to AbstractExProtoHandler |
| close | close conn                          |
| reg   | register client                     |
| pub   | publish message                     |
| sub   | subscribe mytopic qos 1             |
| unsub | unsubscribe mytopicclose            |

输入指令后回车执行，观察控制台输出。

## SDK

`AbstractExProtoHandler`类中，提供了以下方法：

#### send

发送数据至终端连接：

```java
   /**
     * Send a stream of bytes to the connection.
     * <p>
     * These bytes are delivered directly to the associated TCP/UDP socket.
     *
     * @param connection The Connection instance
     * @param data       The bytes
     */
    public static void send(Connection connection, byte[] data) throws Exception;
```
####  terminate

结束终端连接：

```java
    /**
      * Terminate the connection process and TCP/UDP socket.
      *
      * @param connection The Connection instance
      */
    public static void terminate(Connection connection) throws Exception;

```
####  register

注册终端信息：

```java
   /**
     * Register the connection as a Client of EMQ X.
     * <p>
     * This `clientInfo` contains the necessary field information to be an EMQ X client.
     * <p>
     * This method should normally be invoked after confirming that a connection is
     * allowed to access the EMQ X system. For example: after the connection packet
     * has been parsed and authenticated successfully.
     *
     * @param connection Then Connection instance
     * @param clientInfo The Client information
     */
    public static void register(Connection connection, ClientInfo clientInfo) throws Exception;
```

#### publish

推送消息：

```java
    /**
     * The connection Publish a Message to EMQ X
     *
     * @param connection The Connection instance
     * @param message    The Message
     */
    public static void publish(Connection connection, Message message) throws Exception ;
```
#### subscribe

订阅Topic：

```java
    /**
      * The connection Subscribe a Topic to EMQ X
      *
      * @param connection The Connection instance
      * @param topic      The Topic name.
      * @param qos        The subscribed QoS level, available value: 0, 1, 2.
      */
    public static void subscribe(Connection connection, String topic, int qos) throws Exception ;
```
### 不允许重写的SDK方法
以下方法在AbstractExProtoHandler的父类ExProto中被声明，重写这些方法会导致不可知的错误。
```java
loadExProtoHandler(AbstractExProtoHandler exprotoHandler)
```
```java
init(Object conn, Object connInfo)
```
```java
terminated(Object conn, Object reason, Object state)
```
```java
received(Object conn, Object data, Object state)
```
```java
deliver(Object conn, Object msgs0, Object state)
```
```java
loadExProtoHandler(AbstractExProtoHandler exprotoHandler)
```
### 数据封装

#### Connection

​	连接

| Attribute | Type                         |
| --------- | :--------------------------- |
| pid       | com.erlport.erlang.term.Pid  |
| node      | com.erlport.erlang.term.Atom |
| id        | Long                         |
| serial    | Long                         |
| creation  | long                         |

#### ClientInfo

终端信息

| Attribute    | Type   |
| ------------ | ------ |
| protoName    | String |
| protoVersion | String |
| clientId     | String |
| username     | String |
| mountpoint   | String |
| keepalive    | int    |

#### ConnectionInfo

连接信息

| Attribute      | Type                       |
| -------------- | -------------------------- |
| socketType     | io.emqx.exproto.SocketType |
| socketnameIP   | String                     |
| socketnamePort | int                        |
| peernameIP     | String                     |
| peernamePort   | int                        |
| peercert       | io.emqx.exproto.Peercert   |

#### Peercert
Peer cert

| Attribute | Type   |
| --------- | ------ |
| cert_cn   | String |
| cert_dn   | String |

#### Message

EMQ X Broker Message

| Attribute | Type   |
| --------- | ------ |
| id        | String |
| qos       | int    |
| from      | String |
| topic     | String |
| payload   | byte[] |
| timestamp | long   |



## 部署

在完成开发后，需要将 Java 的代码内容部署到 EMQ X 中：

1. 编译扩展程序，将生成的 `.class`  文件（含包结构） 和 SDK 本身的 Jar 包
   拷贝至 EMQ X 安装目录下的 `data/javaexprotodemo` 目录下。
2. 修改 EMQ X 的 `emqx/etc/plugins/emqx_exproto.conf` 配置文件：

``` properties
exproto.listener.protoname = tcp://0.0.0.0:7993
exproto.listener.protoname.driver = java
exproto.listener.protoname.driver_search_path = data/javaexprotodemo
exproto.listener.protoname.driver_callback_module = ExProtoHandlerDemo
```

3. 使用 `./bin/emqx consnole` 启动 EMQ X 并开启 `emqx_exproto` 插件。
4. 接入一个客户端，观察 console 中的输出。

## 特别说明

1. SDK将以Java反射的方式被加载。
2. 必须调用SDK加载处理器（AbstractExProtoHandler的实现类实例化对象），申明被加载的插件：

   ```java 
   ExProto.loadExProtoHandler(AbstractExProtoHandler handler);
   ```
   推荐在无参构造方法中完成，参考`ExProtoHandlerDemo` 的构造方法。

3. 标准输入输出流System.in和System.out用于EMQ X系统内部的交互，请不要在扩展程序中使用。目前可以使用System.err进行控制台打印。