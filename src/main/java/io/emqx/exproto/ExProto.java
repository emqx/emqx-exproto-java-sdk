package io.emqx.exproto;

import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Pid;
import com.erlport.erlang.term.Tuple;

import java.io.Serializable;

public class ExProto {

    static Integer OK = 0;
    static Integer ERROR = 0;
    private static State localState;

    private static AbstractExProtoHandler exprotoHandler;

    public static void loadExProtoHandler(AbstractExProtoHandler exprotoHandler) {
        ExProto.exprotoHandler = exprotoHandler;
    }

    public static Object init(Object conn, Object connInfo) {
        Pid connPid = (Pid) conn;
        ConnectionInfo E = ConnectionInfo.parser(connInfo);
        exprotoHandler.onConnectionEstablished(new Connection(connPid), E);
        Object state = new State();
        return Tuple.two(OK, state);
    }

    public static void terminated(Object conn, Object reason, Object state) {
        Pid connPid = (Pid) conn;
        Binary reasonBinary = (Binary) reason;
        exprotoHandler.onConnectionTerminated(new Connection(connPid), new String(reasonBinary.raw));
    }

    public static Object received(Object conn, Object data, Object state) {
        localState = (State) state;
        Pid connPid = (Pid) conn;
        Binary receivedBinary = (Binary) data;
        exprotoHandler.onConnectionReceived(new Connection(connPid), receivedBinary.raw);
        return Tuple.two(OK, localState);
    }

    public static Object deliver(Object conn, Object msgs0, Object state) {
        Pid connPid = (Pid) conn;
        exprotoHandler.onConnectionDeliver(new Connection(connPid), Message.parser(msgs0));
        return Tuple.two(OK, state);
    }

}

class State implements Serializable {

}
