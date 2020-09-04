package io.emqx.exproto;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;

public class ConnectionInfo {
    private SocketType socketType;
    private String socketnameIP;
    private int socketnamePort;
    private String peernameIP;
    private int peernamePort;
    private Peercert peercert = null;

    public static ConnectionInfo parser(Object connInfo) {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        ArrayList<Object> connInfoList = (ArrayList<Object>) connInfo;
        try {
            for (Object o : connInfoList) {
                if (o instanceof Tuple) {
                    Tuple tupleElement = (Tuple) o;
                    Atom key = (Atom) tupleElement.get(0);
                    switch (key.value) {
                        case "socktype":
                            Atom socketTypeAtom = (Atom) tupleElement.get(1);
                            connectionInfo.setSocketType(SocketType.valueOf(socketTypeAtom.value));
                            break;
                        case "peername":
                            Tuple peernameTupleOutSide = (Tuple) tupleElement.get(1);
                            Tuple peernameTupleInSide = (Tuple) peernameTupleOutSide.get(0);
                            StringBuilder peerNameBuilder = new StringBuilder();
                            for (Integer i = 0; i < peernameTupleInSide.length(); i++) {
                                peerNameBuilder.append(peernameTupleInSide.get(i));
                                if (i != peernameTupleInSide.length() - 1) {
                                    peerNameBuilder.append(".");
                                }
                            }
                            int peername_Port = (int) peernameTupleOutSide.get(1);
                            connectionInfo.setPeernameIP(peerNameBuilder.toString());
                            connectionInfo.setPeernamePort(peername_Port);
                            break;
                        case "sockname":
                            Tuple socketnameTupleOutSide = (Tuple) tupleElement.get(1);
                            Tuple socketnameTupleInSide = (Tuple) socketnameTupleOutSide.get(0);
                            StringBuilder socketIpBuilder = new StringBuilder();
                            for (Integer i = 0; i < socketnameTupleInSide.length(); i++) {
                                socketIpBuilder.append(socketnameTupleInSide.get(i));
                                if (i != socketnameTupleInSide.length() - 1) {
                                    socketIpBuilder.append(".");
                                }
                            }
                            int socketname_Port = (int) socketnameTupleOutSide.get(1);
                            connectionInfo.setSocketnameIP(socketIpBuilder.toString());
                            connectionInfo.setSocketnamePort(socketname_Port);
                            break;
                        case "peercert":
                            Object certKeyObj = tupleElement.get(1);
                            if (certKeyObj instanceof ArrayList) {
                                ArrayList<Object> cert_list = (ArrayList<Object>) certKeyObj;
                                Tuple cert_cn_Tuple = (Tuple) cert_list.get(0);
                                Tuple cert_dn_Tuple = (Tuple) cert_list.get(1);
                                String cert_cn = cert_cn_Tuple.get(1).toString();
                                String cert_dn = cert_dn_Tuple.get(1).toString();
                                connectionInfo.setPeercert(new Peercert(cert_cn, cert_dn));
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[java]  error");
            e.printStackTrace();

        }

        return connectionInfo;
    }

    public ConnectionInfo() {
    }

    public SocketType getSocketType() {
        return socketType;
    }

    public void setSocketType(SocketType socketType) {
        this.socketType = socketType;
    }

    public String getSocketnameIP() {
        return socketnameIP;
    }

    public void setSocketnameIP(String socketnameIP) {
        this.socketnameIP = socketnameIP;
    }

    public String getPeernameIP() {
        return peernameIP;
    }

    public void setPeernameIP(String peernameIP) {
        this.peernameIP = peernameIP;
    }

    public int getPeernamePort() {
        return peernamePort;
    }

    public void setPeernamePort(int peernamePort) {
        this.peernamePort = peernamePort;
    }

    public int getSocketnamePort() {
        return socketnamePort;
    }

    public void setSocketnamePort(int socketnamePort) {
        this.socketnamePort = socketnamePort;
    }

    public Peercert getPeercert() {
        return peercert;
    }

    public void setPeercert(Peercert peercert) {
        this.peercert = peercert;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "socketType=" + socketType +
                ", socketnameIP='" + socketnameIP + '\'' +
                ", socketnamePort=" + socketnamePort +
                ", peernameIP='" + peernameIP + '\'' +
                ", peernamePort=" + peernamePort +
                ", peercert=" + peercert +
                '}';
    }
}
