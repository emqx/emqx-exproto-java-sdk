import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.util.Arrays;

/**
 * transform erlang data type message
 * <p>
 * <p>
 * -type message() :: [ {id, binary()}
 * , {qos, integer()}
 * , {from, binary()}
 * , {topic, binary()}
 * , {payload, binary()}
 * , {timestamp, integer()}
 * ].
 */
public class EmqxDeliverMessage {
    private String id;
    private int qos;
    private String from;
    private String topic;
    private byte[] payload;
    private int timestamp;

    public EmqxDeliverMessage(String id, int qos, String from, String topic, byte[] payload, int timestamp) {
        this.id = id;
        this.qos = qos;
        this.from = from;
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static EmqxDeliverMessage praser(Object msgObj) {
        //todo
        EmqxDeliverMessage emqxDeliverMessage = new EmqxDeliverMessage();

        return emqxDeliverMessage;
    }

    // {id, binary()}
//                   , {qos, integer()}
//                   , {from, binary()}
//                   , {topic, binary()}
//                   , {payload, binary()}
//                   , {timestamp, integer()}
    public static Tuple toErlangDataType(EmqxDeliverMessage deliverMessage) {
        Tuple deliverMessageTuple = new Tuple(6);
        deliverMessageTuple.set(0, Tuple.two(new Atom("id"), new Binary(deliverMessage.getId())));
        deliverMessageTuple.set(1, Tuple.two(new Atom("qos"), deliverMessage.getQos()));
        deliverMessageTuple.set(2, Tuple.two(new Atom("from"), new Binary(deliverMessage.getFrom())));
        deliverMessageTuple.set(3, Tuple.two(new Atom("topic"), new Binary(deliverMessage.getTopic())));
        deliverMessageTuple.set(4, Tuple.two(new Atom("payload"), new Binary(deliverMessage.getPayload())));
        deliverMessageTuple.set(5, Tuple.two(new Atom("timestamp"), deliverMessage.getTimestamp()));
        return deliverMessageTuple;
    }

    public EmqxDeliverMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EmqxDeliverMessage{" +
                "id='" + id + '\'' +
                ", qos=" + qos +
                ", from='" + from + '\'' +
                ", topic='" + topic + '\'' +
                ", payload=" + Arrays.toString(payload) +
                ", timestamp=" + timestamp +
                '}';
    }
}
