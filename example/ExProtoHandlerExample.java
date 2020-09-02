import io.emqx.exproto.*;

import java.util.Arrays;

public class ExProtoHandlerExample extends AbstractExProtoHandler {


    private static ExProtoHandlerExample ExProtoHandler = new ExProtoHandlerExample(new String[]{"don't use [System.in.*]  or [System.out.*]"});

    public ExProtoHandlerExample() {
        ExProto.loadExProtoHandler(ExProtoHandler);
    }

    public ExProtoHandlerExample(String[] args) {

    }

    String help =
            "hello      -->     say hello to AbstractExProtoHandler\r\n" +
                    "close      -->     close conn\r\n" +
                    "reg        -->     register client \r\n" +
                    "pub        -->     publish message\r\n" +
                    "sub        -->     subscribe mytopic qos 1\r\n";

    public void onConnectionEstablished(Connection connection, ConnectionInfo connectionInfo) {
        System.err.println("onConnectionEstablished " + connection.getPid() + "  " + connectionInfo);
        try {
            send(connection, help.getBytes());
        } catch (Exception e) {
        }
    }


    public void onConnectionReceived(Connection connection, byte[] data) {
        String command = new String(data).trim();
        System.err.println(command);
        switch (command) {
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
            case "":
                break;
            case "reg":
                try {
                    ClientInfo clientInfo = new ClientInfo("mqtt", "3.1", "testCID", "testUname", "testMP", 300);
                    register(connection, clientInfo);
                    System.err.println(ClientInfo.toErlangDataType(clientInfo));
                } catch (Exception e) {
                    System.err.println(command + " ERROR");
                }
                break;
            case "pub":
                try {
                    Message message = new Message("testId", 0, "from", "mytopic", "pubmessage".getBytes(), 10);
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

    public void onConnectionTerminated(Connection connection, String reason) {
        System.err.println("onConnectionTerminated " + connection.getPid() + " Reason " + reason);
    }

    public void onConnectionDeliver(Connection connection, Message[] messagesArr) {
        System.err.println("onConnectionDeliver " + connection.getPid() + "  " + Arrays.toString(messagesArr));
        try {
            send(connection, Arrays.toString(messagesArr).getBytes());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
