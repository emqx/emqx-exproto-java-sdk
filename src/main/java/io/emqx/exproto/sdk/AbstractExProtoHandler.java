package io.emqx.exproto.sdk;

import com.erlport.Erlang;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Pid;

/**
 * EMQ X Exproto java SDK;
 * <p>
 * Connection java project to EMQ X Broker.
 * Note 1:
 * Not use "System.in.*" or "System.out.*"  They are used to communicate with EMQ X.
 * Note 2:
 * Invoke  "ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)"
 * or
 * "AbstractExprotoHandler.loadExprotoHandler(AbstractExprotoHandler handler)"
 * <p>
 * load your AbstractExprotoHandler in the "Nonparametric construction method".
 */
public abstract class AbstractExProtoHandler extends ExProtoSDK {

    /*
    TODO :  build your nonparametric construction method,
            and invoke " ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)" ,load your handler in SDK;

            as :
             public AbstractExprotoHandler() {
                AbstractExprotoHandler handler = YourHandler.getInstance();
                ExprotoSDK.loadExprotoHandler(handler);
            }
     */


    /**
     * call back function :
     * Client connect to EMQ X broker.
     *
     * @param connId         Connection Pid (from erlang data type).
     * @param connectionInfo Client information ; include socket type,socket name,peer name,peer cert.
     */
    public abstract void onConnectionEstablished(Pid connId, ConnectionInfo connectionInfo);

    /**
     * call back function:
     * Message data from client.
     *
     * @param connId Connection Pid (from erlang data type).
     * @param data   byte arr.
     */
    public abstract void onConnectionReceived(Pid connId, byte[] data);

    /**
     * call back function:
     * Client connection terminated .
     *
     * @param connId Connection Pid (from erlang data type).
     * @param reason String bytes;
     */
    public abstract void onConnectionTerminated(Pid connId, byte[] reason);

    /**
     * call back function:
     * Receive message from subscribed topic.
     *
     * @param connId      Connection Pid (from erlang data type).
     * @param messagesArr String bytes;
     */
    public abstract void onConnectionDeliver(Pid connId, DeliverMessage[] messagesArr);

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

    /**
     * SDK provide function:
     * Terminate client connection.
     *
     * @param connId Connection Pid (from erlang data type).
     */
    public void terminateConnection(Pid connId) throws Exception {
        Erlang.call("emqx_exproto", "close", new Object[]{connId}, 5000);
    }

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

    public static void loadExprotoHandler(AbstractExProtoHandler handler) {
        ExProtoSDK.loadExProtoHandler(handler);
    }

}
