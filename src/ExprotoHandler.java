public abstract class ExprotoHandler {

    abstract void init(EmqxConnectionInfo connectionInfo);

    abstract void received(long connId, byte[] data);

    abstract void terminated(long connId, byte[] reason);

    abstract void deliver(long connId, EmqxDeliverMessage[] messagesArr);


    public void send(long connId, byte[] data) {
        ExprotoController.ControllerSend(connId, data);
    }

    public void close(long connId) {
        ExprotoController.ControllerClose(connId);
    }

    public void register(long connId, EmqxClientInfo clientInfo) {
        ExprotoController.ControllerRegister(connId, clientInfo);
    }

    public void publish(long connId, EmqxDeliverMessage emqxDeliverMessage) {
        ExprotoController.ControllerPublish(connId, emqxDeliverMessage);
    }

    public void subscribe(long connId, String topic, int qos) {
        ExprotoController.ControllerSubscribe(connId, topic, qos);
    }


}
