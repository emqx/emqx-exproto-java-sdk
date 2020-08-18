package io.emqx.exproto.sdk;

import com.erlport.Erlang;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Pid;
import com.erlport.erlang.term.Tuple;

import java.io.Serializable;

public class ExProtoSDK {

    static Integer OK = 0;
    static Integer ERROR = 0;
    private static State localState;

    private static AbstractExProtoHandler exprotoHandler;

    public static void loadExProtoHandler(AbstractExProtoHandler exprotoHandler) {
        ExProtoSDK.exprotoHandler = exprotoHandler;
    }

    public static Object init(Object conn, Object connInfo) {
        Pid connPid = (Pid) conn;
        ConnectionInfo E = ConnectionInfo.praser(connInfo);
        exprotoHandler.onConnectionEstablished(connPid, E);
        Object state = new State();
        try {
            Erlang.call("emqx_exproto", "subscribe", new Object[]{conn, new Binary("t/dn"), 0}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error subscribe: %s\n", e);
        }
        return Tuple.two(OK, state);
    }

    public static void terminated(Object conn, Object reason, Object state) {
        Pid connPid = (Pid) conn;
        Binary reasonBinary = (Binary) reason;
        exprotoHandler.onConnectionTerminated(connPid, reasonBinary.raw);
    }

    public static Object received(Object conn, Object data, Object state) {
        localState = (State) state;
        localState.incr();
        Pid connPid = (Pid) conn;
        Binary receivedBinary = (Binary) data;
        exprotoHandler.onConnectionReceived(connPid, receivedBinary.raw);
        return Tuple.two(OK, localState);
    }

    public static Object deliver(Object conn, Object msgs0, Object state) {
        Pid connPid = (Pid) conn;
        exprotoHandler.onConnectionDeliver(connPid, DeliverMessage.praser(msgs0));
        return Tuple.two(OK, state);
    }

}

class State implements Serializable {

    Integer times;

    public State() {
        times = 0;
    }

    public Integer incr() {
        times += 1;
        return times;
    }

    @Override
    public String toString() {
        return String.format("io.emqx.exproto.State(times: %d)", times);
    }
}
