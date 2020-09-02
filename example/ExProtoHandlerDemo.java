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

    private static ExProtoHandlerDemo handlerDemo = new ExProtoHandlerDemo(new String[]{"Don't use [System.in.*] or [System.out.*]"});

    public ExProtoHandlerDemo() {
        ExProto.loadExProtoHandler(handlerDemo);
    }

    public ExProtoHandlerDemo(String[] args) {
        for (String arg : args) {
            //use [System.err.*] is fine
            System.err.println(arg);
        }
    }

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
        System.err.println(connection);
        System.err.println(connectionInfo);
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
        System.err.println(connection);
        System.err.println(new String(data));
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
        System.err.println(connection);
        System.err.println(reason);
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
        System.err.println(connection);
        for (Message message : messagesArr) {
            System.err.println(message);
        }
    }
}
