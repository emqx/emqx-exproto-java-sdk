package io.emqx.exproto.sdk;

import com.erlport.erlang.term.Pid;
/**
 * EMQ X Exproto java SDK;
 * <p>
 * Connection java project to EMQ X Broker.
 * Note 1:
 * Not use "System.in.*" or "System.out.*"  They are used to communicate with EMQ X.
 * Note 2:
 * Invoke  "ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)" load your AbstractExprotoHandler in the "Nonparametric construction method".
 */
public class ExProtoHandlerDemo extends AbstractExProtoHandler {
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
