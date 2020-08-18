import com.erlport.erlang.term.Pid;
import io.emqx.exproto.sdk.AbstractExprotoHandler;
import io.emqx.exproto.sdk.ConnectionInfo;
import io.emqx.exproto.sdk.DeliverMessage;
import io.emqx.exproto.sdk.ExprotoSDK;

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
 * <p>
 * <p>
 * SDK provide function:
 * <p>
 * void sendToConnection(Pid connId, byte[] data)
 * <p>
 * void terminateConnection(Pid connId)
 * <p>
 * void registerClient(Pid connId, ClientInfo clientInfo)
 * <p>
 * void publishMessage(Pid connPid, DeliverMessage message)
 * <p>
 * void subscribeTopic(Pid connId, String topic, int qos)
 */
public class ExprotoHandlerDemo extends AbstractExprotoHandler {
   /*
    TODO :  build your nonparametric construction method,
            and invoke " ExprotoSDK.loadExprotoHandler(AbstractExprotoHandler handler)" ,load your handler in SDK;

            as :
             public ExprotoHandlerDemo() {
                AbstractExprotoHandler handler =new ExprotoHandlerDemo("ExprotoHandler Name");
                ExprotoSDK.loadExprotoHandler(handler);
            }
     */

    public ExprotoHandlerDemo() {
        AbstractExprotoHandler handler = new ExprotoHandlerDemo("ExprotoHandler Name");
        ExprotoSDK.loadExprotoHandler(handler);
    }

    String name;

    public ExprotoHandlerDemo(String name) {
        this.name = name;
    }

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
