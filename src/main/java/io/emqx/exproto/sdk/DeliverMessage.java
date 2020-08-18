package io.emqx.exproto.sdk;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;
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
public class DeliverMessage {
    private String id;
    private int qos;
    private String from;
    private String topic;
    private byte[] payload;
    private int timestamp;

    public DeliverMessage(String id, int qos, String from, String topic, byte[] payload, int timestamp) {
        this.id = id;
        this.qos = qos;
        this.from = from;
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static DeliverMessage[] praser(Object msgObj) {
        ArrayList<DeliverMessage> deliverMessageArrayList = new ArrayList<>();
        if (msgObj instanceof ArrayList) {
            ArrayList<Object> messageDetailList_list = (ArrayList<Object>) msgObj;
            for (Object o : messageDetailList_list) {
                ArrayList<Object>messageDetailList = (ArrayList<Object> ) o;
                deliverMessageArrayList.add(praserOne(messageDetailList.toArray(new Tuple[6])));
            }
        }
        return deliverMessageArrayList.toArray(new DeliverMessage[deliverMessageArrayList.size()]);
    }

    private static DeliverMessage praserOne(Tuple[] messgaeTuplesArr) {
        DeliverMessage deliverMessage = new DeliverMessage();
        for (Tuple tuple : messgaeTuplesArr) {
            Atom key = (Atom) tuple.get(0);
            Object value = tuple.get(1);
            Binary value_b;
            switch (key.value) {
                case "id":
                    value_b = (Binary) value;
                    deliverMessage.setId(new String(value_b.raw));
                    break;
                case "qos":
                    deliverMessage.setQos((int) value);
                    break;
                case "from":
                    value_b = (Binary) value;
                    deliverMessage.setFrom(new String(value_b.raw));
                    break;
                case "topic":
                    value_b = (Binary) value;
                    deliverMessage.setTopic(new String(value_b.raw));
                    break;
                case "payload":
                    value_b = (Binary) value;
                    deliverMessage.setPayload(value_b.raw);
                    break;
                case "timestamp":
                    deliverMessage.setTimestamp((int) value);
                    break;
            }
        }
        return deliverMessage;
    }


    // {id, binary()}
//                   , {qos, integer()}
//                   , {from, binary()}
//                   , {topic, binary()}
//                   , {payload, binary()}
//                   , {timestamp, integer()}
//    public static Tuple toErlangDataType(DeliverMessage deliverMessage) {
//        Tuple deliverMessageTuple = new Tuple(6);
//        deliverMessageTuple.set(0, Tuple.two(new Atom("id"), new Binary(deliverMessage.getId())));
//        deliverMessageTuple.set(1, Tuple.two(new Atom("qos"), deliverMessage.getQos()));
//        deliverMessageTuple.set(2, Tuple.two(new Atom("from"), new Binary(deliverMessage.getFrom())));
//        deliverMessageTuple.set(3, Tuple.two(new Atom("topic"), new Binary(deliverMessage.getTopic())));
//        deliverMessageTuple.set(4, Tuple.two(new Atom("payload"), new Binary(deliverMessage.getPayload())));
//        deliverMessageTuple.set(5, Tuple.two(new Atom("timestamp"), deliverMessage.getTimestamp()));
//        return deliverMessageTuple;
//    }

    public static ArrayList<Tuple> toErlangDataType(DeliverMessage deliverMessage) {
        ArrayList<Tuple> tupleArrayList = new ArrayList<>();
        tupleArrayList.add(Tuple.two(new Atom("id"), new Binary(deliverMessage.getId())));
        tupleArrayList.add(Tuple.two(new Atom("qos"), deliverMessage.getQos()));
        tupleArrayList.add(Tuple.two(new Atom("from"), new Binary(deliverMessage.getFrom())));
        tupleArrayList.add(Tuple.two(new Atom("topic"), new Binary(deliverMessage.getTopic())));
        tupleArrayList.add(Tuple.two(new Atom("payload"), new Binary(deliverMessage.getPayload())));
        tupleArrayList.add(Tuple.two(new Atom("timestamp"), deliverMessage.getTimestamp()));
        return tupleArrayList;
    }

    public DeliverMessage() {
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

