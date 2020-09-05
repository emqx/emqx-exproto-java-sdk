package io.emqx.exproto;

import com.erlport.Erlang;
import com.erlport.erlang.term.Binary;

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
public abstract class AbstractExProtoHandler extends ExProto {

    /*
    TODO :  build your nonparametric construction method,
            and invoke " ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)" ,load your handler in SDK;

            as :
            public AbstractExProtoHandler() {
                ExProto.loadExProtoHandler(ExProtoHandler);
            }
     */


    /**
     * A connection established.
     * <p>
     * This function will be scheduled after a TCP connection established to EMQ X
     * or receive a new UDP socket.
     *
     * @param connection     The Connection instance
     * @param connectionInfo The Connection information
     */
    public abstract void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo);

    /**
     * A connection received bytes.
     * <p>
     * This callback will be scheduled when a connection received bytes from TCP/UDP socket.
     *
     * @param connection The Connection instance
     * @param data       The bytes array
     */
    public abstract void onConnectionReceived(Connection connection, byte[] data);

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
    public abstract void onConnectionTerminated(Connection connection, String reason);

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
    public abstract void onConnectionDeliver(Connection connection, Message[] messagesArr);

    /**
     * Send a stream of bytes to the connection.
     * <p>
     * These bytes are delivered directly to the associated TCP/UDP socket.
     *
     * @param connection The Connection instance
     * @param data       The bytes
     */
    public static void send(Connection connection, byte[] data) throws Exception {
        Erlang.call("emqx_exproto", "send", new Object[]{connection.getPid(), new Binary(data)}, 5000);
    }

    /**
     * Terminate the connection process and TCP/UDP socket.
     *
     * @param connection The Connection instance
     */
    public static void terminate(Connection connection) throws Exception {
        Erlang.call("emqx_exproto", "close", new Object[]{connection.getPid()}, 5000);
    }

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
    public static void register(Connection connection, ClientInfo clientInfo) throws Exception {
        Erlang.call("emqx_exproto", "register", new Object[]{connection.getPid(), ClientInfo.toErlangDataType(clientInfo)}, 5000);
    }

    /**
     * The connection Publish a Message to EMQ X
     *
     * @param connection The Connection instance
     * @param message    The Message
     */
    public static void publish(Connection connection, Message message) throws Exception {
        Erlang.call("emqx_exproto", "publish", new Object[]{connection.getPid(), Message.toErlangDataType(message)}, 5000);
    }

    /**
     * The connection Subscribe a Topic to EMQ X
     *
     * @param connection The Connection instance
     * @param topic      The Topic name.
     * @param qos        The subscribed QoS level, available value: 0, 1, 2.
     */
    public static void subscribe(Connection connection, String topic, int qos) throws Exception {
        Erlang.call("emqx_exproto", "subscribe", new Object[]{connection.getPid(), new Binary(topic), qos}, 5000);
    }

    /**
     * The Connection UnSubscribe a Topic to EMQ X
     *
     * @param connection The Connection instance
     * @param topic      The Topic name.
     */
    public static void unsubscribe(Connection connection, String topic) throws Exception {
        Erlang.call("emqx_exproto", "unsubscribe", new Object[]{connection.getPid(), new Binary(topic)}, 5000);
    }

}
