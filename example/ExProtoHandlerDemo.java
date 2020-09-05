import java.math.BigInteger;
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
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "pub":
                try {
                    Message message =
                            new Message("testId", 0, "from", "mytopic", "pubmessage".getBytes(), new BigInteger("" + System.currentTimeMillis()));
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
            send(connection, Arrays.toString(messagesArr).getBytes());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
