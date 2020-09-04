package io.emqx.exproto;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Message {
    private String id;
    private int qos;
    private String from;
    private String topic;
    private byte[] payload;
    private BigInteger timestamp;

    public Message(String id, int qos, String from, String topic, byte[] payload, BigInteger timestamp) {
        this.id = id;
        this.qos = qos;
        this.from = from;
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static Message[] parser(Object msgObj) {
        ArrayList<Message> messageArrayList = new ArrayList<>();
        if (msgObj instanceof ArrayList) {
            ArrayList<Object> messageDetailList_list = (ArrayList<Object>) msgObj;
            for (Object o : messageDetailList_list) {
                ArrayList<Object> messageDetailList = (ArrayList<Object>) o;
                messageArrayList.add(parserOne(messageDetailList.toArray(new Tuple[6])));
            }
        }
        return messageArrayList.toArray(new Message[messageArrayList.size()]);
    }

    private static Message parserOne(Tuple[] messageTuplesArr) {
        Message message = new Message();
        for (Tuple tuple : messageTuplesArr) {
            Atom key = (Atom) tuple.get(0);
            Object value = tuple.get(1);
            Binary value_b;
            switch (key.value) {
                case "id":
                    value_b = (Binary) value;
                    message.setId(new String(value_b.raw));
                    break;
                case "qos":
                    message.setQos((int) value);
                    break;
                case "from":
                    value_b = (Binary) value;
                    message.setFrom(new String(value_b.raw));
                    break;
                case "topic":
                    value_b = (Binary) value;
                    message.setTopic(new String(value_b.raw));
                    break;
                case "payload":
                    value_b = (Binary) value;
                    message.setPayload(value_b.raw);
                    break;
                case "timestamp":
                    message.setTimestamp((BigInteger) value);
                    break;
            }
        }
        return message;
    }


    public static ArrayList<Tuple> toErlangDataType(Message message) {
        ArrayList<Tuple> tupleArrayList = new ArrayList<>();
        tupleArrayList.add(Tuple.two(new Atom("id"), new Binary(message.getId())));
        tupleArrayList.add(Tuple.two(new Atom("qos"), message.getQos()));
        tupleArrayList.add(Tuple.two(new Atom("from"), new Binary(message.getFrom())));
        tupleArrayList.add(Tuple.two(new Atom("topic"), new Binary(message.getTopic())));
        tupleArrayList.add(Tuple.two(new Atom("payload"), new Binary(message.getPayload())));
        tupleArrayList.add(Tuple.two(new Atom("timestamp"), message.getTimestamp()));
        return tupleArrayList;
    }

    public Message() {
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

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
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

