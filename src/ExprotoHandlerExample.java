public class ExprotoHandlerExample extends ExprotoHandler {

    private static EmqxDeliverMessage emqxDeliverMessage = new EmqxDeliverMessage("msdId1", 0, "javasdk", "myTopic", "mymessage".getBytes(), 5);
    private static EmqxClientInfo clientInfo2 = new EmqxClientInfo("lw", 1, "clientId2", "username2", "mp2", 300);
    private static String help = "l  -> list conn\n" +
            "sub  ->  subscribe myTopic qos 0\n" +
            "pub  ->  publish a message \n" +
            "reg  ->  register a client \n" +
            "close->  close self conn\n" +
            "send ->  sdk will send you a message";

    @Override
    void init(EmqxConnectionInfo connectionInfo) {
        System.err.println("[java] handler  get connInfo " + connectionInfo);
        responseClient(connectionInfo.getConnId(), help);

    }

    @Override
    void received(long connId, byte[] data) {
        String command = new String(data).trim();
        System.err.println(command);
        switch (command) {
            case "close":
                close(connId);
                break;
            case "send":
                responseClient(connId, "Java SDK send message to you : hello my friend");
                break;
            case "reg":
                register(connId, clientInfo2);
                responseClient(connId, clientInfo2.toString());
                break;
            case "pub":
                System.err.println("[java] ExprotoHandlerExample pub message ");
                publish(connId, emqxDeliverMessage);
                responseClient(connId, emqxDeliverMessage.toString());
            case "sub":
                System.err.println("[java] ExprotoHandlerExample sub ");
                subscribe(connId, "myTopic", 0);
                responseClient(connId, "subscribe [myTopic] over");
                break;
            case "l":
                responseClient(connId, ExprotoController.listConnMapping());
                break;
            case "help":
                responseClient(connId, help);
                break;
            default:
                responseClient(connId, "no such command " + command);
                break;
        }
    }

    private void responseClient(long connId, String message) {
        message = message + "\n";
        send(connId, message.getBytes());
    }

    @Override
    void terminated(long connId, byte[] reason) {
        System.err.println("[java] handler XXXX terminated " + connId + " Reason " + new String(reason));
    }

    @Override
    void deliver(long connId, EmqxDeliverMessage[] messagesArr) {
        System.err.println("[java] handler deliver " + connId);
        for (EmqxDeliverMessage emqxDeliverMessage : messagesArr) {
            System.err.println("emqxDeliverMessage " + emqxDeliverMessage.toString());
        }
    }
}
