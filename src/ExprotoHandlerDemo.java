import com.erlport.erlang.term.Pid;
import io.emqx.exproto.sdk.AbstractExprotoHandler;
import io.emqx.exproto.sdk.ConnectionInfo;
import io.emqx.exproto.sdk.DeliverMessage;
import io.emqx.exproto.sdk.ExprotoSDK;

public class ExprotoHandlerDemo extends AbstractExprotoHandler {

    private ExprotoHandlerDemo handlerDemo = new ExprotoHandlerDemo("myhandler");

    public ExprotoHandlerDemo(String name) {
    }


    public ExprotoHandlerDemo() {
        ExprotoSDK.loadExprotoHandler(handlerDemo);
    }

    @Override
    public void onConnectionEstablished(Pid pid, ConnectionInfo connectionInfo) {

    }

    @Override
    public void onConnectionReceived(Pid pid, byte[] bytes) {

    }

    @Override
    public void onConnectionTerminated(Pid pid, byte[] bytes) {

    }

    @Override
    public void onConnectionDeliver(Pid pid, DeliverMessage[] deliverMessages) {

    }
}
