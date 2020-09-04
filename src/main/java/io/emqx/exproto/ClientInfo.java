package io.emqx.exproto;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;

public class ClientInfo {
    private String protoName;
    private String protoVersion;
    private String clientId;
    private String username;
    private String mountpoint;
    private int keepalive;


    public ClientInfo(String protoName, String protoVersion, String clientId, String username, String mountpoint, int keepalive) {
        this.protoName = protoName;
        this.setProtoVersion(protoVersion);
        this.clientId = clientId;
        this.username = username;
        this.mountpoint = mountpoint;
        this.keepalive = keepalive;
    }

    public static ArrayList<Tuple> toErlangDataType(ClientInfo clientInfo) {
        ArrayList<Tuple> clientInfoList = new ArrayList<>();
        clientInfoList.add(Tuple.two(new Atom("proto_name"), new Binary(clientInfo.getProtoName())));
        clientInfoList.add(Tuple.two(new Atom("proto_ver"), new Binary(clientInfo.getProtoVersion())));
        clientInfoList.add(Tuple.two(new Atom("clientid"), new Binary(clientInfo.getClientId())));
        clientInfoList.add(Tuple.two(new Atom("username"), new Binary(clientInfo.getUsername())));
        clientInfoList.add(Tuple.two(new Atom("mountpoint"), new Binary(clientInfo.getMountpoint())));
        clientInfoList.add(Tuple.two(new Atom("keepalive"), clientInfo.getKeepalive()));
        return clientInfoList;
    }

    public ClientInfo() {
    }

    public String getProtoName() {
        return protoName;
    }

    public void setProtoName(String protoName) {
        this.protoName = protoName;
    }

    public String getProtoVersion() {
        return protoVersion;
    }

    public void setProtoVersion(String protoVersion) {
        this.protoVersion = protoVersion;
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
                "protoName='" + protoName + '\'' +
                ", protoVersion='" + protoVersion + '\'' +
                ", clientId='" + clientId + '\'' +
                ", userName='" + username + '\'' +
                ", mountPoint='" + mountpoint + '\'' +
                ", keepAlive=" + keepalive +
                '}';
    }
}
