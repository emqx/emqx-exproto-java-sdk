package io.emqx.exproto;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;

public class ClientInfo {
    private String protoName;
    private String protoVersion;
    private String clientId;
    private String userName;
    private String mountPoint;
    private int keepAlive;


    public ClientInfo(String protoName, String protoVersion, String clientId, String userName, String mountPoint, int keepAlive) {
        this.protoName = protoName;
        this.setProtoVersion(protoVersion);
        this.clientId = clientId;
        this.userName = userName;
        this.mountPoint = mountPoint;
        this.keepAlive = keepAlive;
    }

    public static ArrayList<Tuple> toErlangDataType(ClientInfo clientInfo) {
        ArrayList<Tuple> clientInfoList = new ArrayList<>();
        clientInfoList.add(Tuple.two(new Atom("proto_name"), new Binary(clientInfo.getProtoName())));
        clientInfoList.add(Tuple.two(new Atom("proto_ver"), new Binary(clientInfo.getProtoVersion())));
        clientInfoList.add(Tuple.two(new Atom("clientid"), new Binary(clientInfo.getClientId())));
        clientInfoList.add(Tuple.two(new Atom("username"), new Binary(clientInfo.getUserName())));
        clientInfoList.add(Tuple.two(new Atom("mountpoint"), new Binary(clientInfo.getMountPoint())));
        clientInfoList.add(Tuple.two(new Atom("keepalive"), clientInfo.getKeepAlive()));
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "protoName='" + protoName + '\'' +
                ", protoVersion='" + protoVersion + '\'' +
                ", clientId='" + clientId + '\'' +
                ", userName='" + userName + '\'' +
                ", mountPoint='" + mountPoint + '\'' +
                ", keepAlive=" + keepAlive +
                '}';
    }
}
