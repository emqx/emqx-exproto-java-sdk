import com.erlport.Erlang;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Pid;
import com.erlport.erlang.term.Tuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ExprotoController {

    private static HashMap<Long, Pid> connMapping = new HashMap<>();

    static Integer OK = 0;
    static Integer ERROR = 0;

    private static ExprotoHandler handler = new ExprotoHandlerExample();

    public static void setExprotoHandler(ExprotoHandler exprotoHandler) {
        handler = exprotoHandler;
    }

    private static State localState;


    public static void ControllerSend(long connId, byte[] data) {
        Binary dataBinary = new Binary(data);
        Pid connPid = findConnPidFromConnMapping(connId);
        send(connPid, dataBinary);
    }

    public static void ControllerClose(long connId) {
        Pid conn = findConnPidFromConnMapping(connId);
        close(conn);
    }

    public static void ControllerRegister(long connId, EmqxClientInfo clientInfoArr) {
        Pid connPid = findConnPidFromConnMapping(connId);
        register(connPid, EmqxClientInfo.toErlangDataType(clientInfoArr));
    }

    public static void ControllerPublish(long connId, EmqxDeliverMessage message) {
        Pid connPid = findConnPidFromConnMapping(connId);
        publish(connPid, EmqxDeliverMessage.toErlangDataType(message));
    }

    public static void ControllerSubscribe(long connId, String topic, int qos) {
        Pid connPid = findConnPidFromConnMapping(connId);
        subscribe(connPid, topic, qos);
    }

    public static Object init(Object conn, Object connInfo) {
        Object state = new State();
        subscribe(conn, new Binary("t/dn"), 0);
        Pid connPid = (Pid) conn;
        EmqxConnectionInfo E = EmqxConnectionInfo.praser(connPid, connInfo);
        putPid2ConnMapping(connPid);
        handler.init(E);
        return Tuple.two(OK, state);
    }

    public static void terminated(Object conn, Object reason, Object state) {
        System.err.println("[java] logic level terminated " + conn);
        Pid connPid = (Pid) conn;
        Binary reasonBinary = (Binary) reason;
        removePidFromConnMapping(connPid.id);
        handler.terminated(connPid.id, reasonBinary.raw);
    }

    public static Object received(Object conn, Object data, Object state) {
        localState = (State) state;
        localState.incr();
        Pid connPid = (Pid) conn;
        Binary receivedBinary = (Binary) data;
        handler.received(connPid.id, receivedBinary.raw);
        return Tuple.two(OK, localState);
    }


    public static void send(Object conn, Object data) {
        try {
            Erlang.call("emqx_exproto", "send", new Object[]{conn, data}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error: %s\n", e);
        }
    }

    public static void close(Object conn) {
        try {
            Erlang.call("emqx_exproto", "close", new Object[]{conn}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error: %s\n", e);
        }
        return;
    }


    public static void register(Object conn, Object clientInfo) {
        try {
            Erlang.call("emqx_exproto", "register", new Object[]{conn, clientInfo}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error: %s\n", e);
        }
        return;
    }


    public static void publish(Object conn, Object message) {
        try {
            Erlang.call("emqx_exproto", "publish", new Object[]{conn, message}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error: %s\n", e);
        }
    }


    public static void subscribe(Object conn, Object topic, Object qos) {
        try {
            Erlang.call("emqx_exproto", "subscribe", new Object[]{conn, topic, qos}, 5000);
        } catch (Exception e) {
            System.err.printf("[java] send data error: %s\n", e);
        }
    }


    public static Object deliver(Object conn, Object msgs0, Object state) {
        List<Object> msgs = (List<Object>) msgs0;
        for (Object msg : msgs) {
            publish(conn, msg);
        }
        localState = (State) state;
        localState.incr();
        Pid connPid = (Pid) conn;
        EmqxDeliverMessage[] emqxDeliverMessages = new EmqxDeliverMessage[msgs.size()];
        for (int i = 0; i < msgs.size(); i++) {
            emqxDeliverMessages[i] = EmqxDeliverMessage.praser(msgs.get(i));
        }
        handler.deliver(connPid.id, emqxDeliverMessages);
        return Tuple.two(OK, localState);
    }

    private static Pid findConnPidFromConnMapping(long connId) {
        return connMapping.get(connId);
    }

    private static void putPid2ConnMapping(Pid conn) {
        connMapping.put(conn.id, conn);
    }

    private static void removePidFromConnMapping(long connId) {
        connMapping.remove(connId);
    }

    public static String listConnMapping() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Long aLong : connMapping.keySet()) {
            stringBuilder.append(aLong);
            stringBuilder.append(":");
            stringBuilder.append(connMapping.get(aLong));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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