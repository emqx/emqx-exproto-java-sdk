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
