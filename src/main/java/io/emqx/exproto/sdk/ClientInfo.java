package io.emqx.exproto.sdk;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;

public class ClientInfo {
    private String proto_name;
    private boolean use_str_proto_ver = false;
    private String proto_ver_str;
    private int proto_ver_int;
    private String clientId;
    private String username;
    private String mountpoint;
    private int keepalive;


    public ClientInfo(String proto_name, String proto_ver_str, String clientId, String username, String mountpoint, int keepalive) {
        this.proto_name = proto_name;
        this.setProto_ver_str(proto_ver_str);
        this.clientId = clientId;
        this.username = username;
        this.mountpoint = mountpoint;
        this.keepalive = keepalive;
    }

    public ClientInfo(String proto_name, int proto_ver_int, String clientId, String username, String mountpoint, int keepalive) {
        this.proto_name = proto_name;
        this.setProto_ver_int(proto_ver_int);
        this.clientId = clientId;
        this.username = username;
        this.mountpoint = mountpoint;
        this.keepalive = keepalive;
    }

//    public static Tuple toErlangDataType(ClientInfo clientInfo) {
//        Tuple clientInfoTuple = new Tuple(6);
//        clientInfoTuple.set(0, Tuple.two(new Atom("proto_name"), new Binary(clientInfo.getProto_name())));
//        if (clientInfo.use_str_proto_ver) {
//            clientInfoTuple.set(1, Tuple.two(new Atom("proto_ver"), new Binary(clientInfo.getProto_ver_str())));
//        } else {
//            clientInfoTuple.set(1, Tuple.two(new Atom("proto_ver"), clientInfo.getProto_ver_int()));
//        }
//        clientInfoTuple.set(2, Tuple.two(new Atom("clientid"), new Binary(clientInfo.getClientId())));
//        clientInfoTuple.set(3, Tuple.two(new Atom("username"), new Binary(clientInfo.getUsername())));
//        clientInfoTuple.set(4, Tuple.two(new Atom("mountpoint"), new Binary(clientInfo.getMountpoint())));
//        clientInfoTuple.set(5, Tuple.two(new Atom("keepalive"), clientInfo.getKeepalive()));
//        return clientInfoTuple;
//    }
    public static ArrayList<Tuple> toErlangDataType(ClientInfo clientInfo) {
        ArrayList<Tuple> clientInfoList = new ArrayList<>();
        clientInfoList.add(Tuple.two(new Atom("proto_name"), new Binary(clientInfo.getProto_name())));
        if(clientInfo.use_str_proto_ver){
            clientInfoList.add(Tuple.two(new Atom("proto_ver"), new Binary(clientInfo.getProto_ver_str())));
        }else {
            clientInfoList.add(Tuple.two(new Atom("proto_ver"), clientInfo.getProto_ver_int()));
        }
        clientInfoList.add(Tuple.two(new Atom("clientid"), new Binary(clientInfo.getClientId())));
        clientInfoList.add(Tuple.two(new Atom("username"), new Binary(clientInfo.getUsername())));
        clientInfoList.add(Tuple.two(new Atom("mountpoint"), new Binary(clientInfo.getMountpoint())));
        clientInfoList.add(Tuple.two(new Atom("keepalive"), clientInfo.getKeepalive()));
        return clientInfoList;
    }

    public ClientInfo() {
    }

    public String getProto_name() {
        return proto_name;
    }

    public void setProto_name(String proto_name) {
        this.use_str_proto_ver = true;
        this.proto_name = proto_name;
    }

    public String getProto_ver_str() {
        return proto_ver_str;
    }

    public void setProto_ver_str(String proto_ver_str) {
        this.proto_ver_str = proto_ver_str;
    }

    public int getProto_ver_int() {
        return proto_ver_int;
    }

    public void setProto_ver_int(int proto_ver_int) {
        this.use_str_proto_ver = false;
        this.proto_ver_int = proto_ver_int;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "proto_name='" + proto_name + '\'' +
                ", use_str_proto_ver=" + use_str_proto_ver +
                ", proto_ver_str='" + proto_ver_str + '\'' +
                ", proto_ver_int=" + proto_ver_int +
                ", clientId='" + clientId + '\'' +
                ", username='" + username + '\'' +
                ", mountpoint='" + mountpoint + '\'' +
                ", keepalive=" + keepalive +
                '}';
    }

}
