import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Pid;
import io.emqx.exproto.sdk.*;

import java.util.Arrays;

public class ExProtoHandlerExample extends AbstractExProtoHandler {
    String help =
            "close      -->     close conn\r\n" +
                    "reg        -->     register client \r\n" +
                    "pub        -->     publish message\r\n" +
                    "sub        -->     subscribe mytopic qos 1\r\n";

    private static ExProtoHandlerExample exProtoHandler = new ExProtoHandlerExample(new String[]{"don't use [System.in.*]  or [System.out.*]"});

    public ExProtoHandlerExample() {
        ExProtoSDK.loadExProtoHandler(exProtoHandler);
    }

    public ExProtoHandlerExample(String[] args) {
        for (String arg : args) {
            System.err.println(arg);
        }
    }

    @Override
    public void onConnectionEstablished(Pid connId, ConnectionInfo connectionInfo) {
        System.err.println("onConnectionEstablished " + connId + "  " + connectionInfo);
        try {
            sendToConnection(connId, help.getBytes());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onConnectionReceived(Pid connId, byte[] data) {
        String command = new String(data).trim();
        System.err.println(command);
        try {
            switch (command) {
                case "hello":
                    sendToConnection(connId, ("hello my friend " + connId.id + "\r\n").getBytes());
                    break;
                case "close":
                    sendToConnection(connId, ("goodbye my friend " + connId.id + "\r\n").getBytes());
                    terminateConnection(connId);
                    break;
                case "":
                    sendToConnection(connId, ("what do you want? " + connId.id + "\r\n").getBytes());
                    break;
                case "reg":
                    ClientInfo clientInfo = new ClientInfo("mqtt", 3, "testCID", "testUname", "testMP", 300);
                    registerClient(connId, clientInfo);
                    System.err.println(ClientInfo.toErlangDataType(clientInfo));
                    break;
                case "pub":
                    DeliverMessage message = new DeliverMessage("testId", 0, "from", "mytopic", "pubmessage".getBytes(), 10);
                    publishMessage(connId, message);
                    System.err.println(DeliverMessage.toErlangDataType(message));
                    break;
                case "sub":
                    String topic = "mytopic";
                    subscribeTopic(connId, topic, 1);
                    System.err.println(new Binary(topic));
                    break;
                case "help":
                    sendToConnection(connId, help.getBytes());
                    break;
                default:
                    sendToConnection(connId, ("i don't know " + command + "\r\n").getBytes());
                    break;

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onConnectionTerminated(Pid connId, byte[] reason) {
        System.err.println("onConnectionTerminated " + connId + " reason len " + reason.length + "  " + new String(reason));
    }

    @Override
    public void onConnectionDeliver(Pid connId, DeliverMessage[] messagesArr) {
        System.err.println("onConnectionDeliver " + connId + "  " + Arrays.toString(messagesArr));
    }
}
