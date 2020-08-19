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
     * call back function :
     * Client connect to EMQ X broker.
     *
     * @param connection     Connection Pid (from erlang data type).
     * @param connectionInfo Client information ; include socket type,socket name,peer name,peer cert.
     */
    public abstract void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo);

    /**
     * call back function:
     * Message data from client.
     *
     * @param connection Connection Pid (from erlang data type).
     * @param data       byte arr.
     */
    public abstract void onConnectionReceived(Connection connection, byte[] data);

    /**
     * call back function:
     * Client connection terminated .
     *
     * @param connection Connection Pid (from erlang data type).
     * @param reason     String bytes;
     */
    public abstract void onConnectionTerminated(Connection connection, byte[] reason);

    /**
     * call back function:
     * Receive message from subscribed topic.
     *
     * @param connection  Connection Pid (from erlang data type).
     * @param messagesArr String bytes;
     */
    public abstract void onConnectionDeliver(Connection connection, DeliverMessage[] messagesArr);

    /**
     * SDK provide function:
     * Send message to client connection
     *
     * @param connection Connection Pid (from erlang data type).
     * @param data       Your message data byte arr.
     */
    public static void send(Connection connection, byte[] data) throws Exception {
        Erlang.call("emqx_exproto", "send", new Object[]{connection.getPid(), new Binary(data)}, 5000);
    }

    /**
     * SDK provide function:
     * Terminate client connection.
     *
     * @param connection Connection Pid (from erlang data type).
     */
    public static void terminate(Connection connection) throws Exception {
        Erlang.call("emqx_exproto", "close", new Object[]{connection.getPid()}, 5000);
    }

    /**
     * SDK provide function:
     * Register a client in EMQ X Broker.
     *
     * @param connection Connection Pid (from erlang data type).
     * @param clientInfo client information, include protocol name, protocol version ,client Id,username,mount point,keep alive time.
     */
    public static void register(Connection connection, ClientInfo clientInfo) throws Exception {
        Erlang.call("emqx_exproto", "register", new Object[]{connection.getPid(), ClientInfo.toErlangDataType(clientInfo)}, 5000);
    }

    /**
     * SDK provide function:
     * Publish message to EMQ X Broker
     *
     * @param connection Connection Pid (from erlang data type).
     * @param message    Message information,include message id,qos,from,topic,payload,timestamp.
     */
    public static void publish(Connection connection, DeliverMessage message) throws Exception {
        Erlang.call("emqx_exproto", "publish", new Object[]{connection.getPid(), DeliverMessage.toErlangDataType(message)}, 5000);
    }

    /**
     * SDK provide function:
     * Subscribe topic in EMQ X Broker
     *
     * @param connection Connection Pid (from erlang data type).
     * @param topic      Topic name.
     * @param qos        qos.
     */
    public static void subscribe(Connection connection, String topic, int qos) throws Exception {
        Erlang.call("emqx_exproto", "subscribe", new Object[]{connection.getPid(), new Binary(topic), qos}, 5000);
    }

    public static void loadExProtoHandler(AbstractExProtoHandler handler) {
        loadExProtoHandler(handler);
    }

}
