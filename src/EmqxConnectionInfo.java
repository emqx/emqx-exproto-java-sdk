import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Pid;
import com.erlport.erlang.term.Tuple;

import java.util.ArrayList;

/**
 * transform erlang data type conninfo
 * <p>
 * -type conninfo() :: [ {socktype, tcp | tls | udp | dtls},
 * , {peername, {inet:ip_address(), inet:port_number()}},
 * , {sockname, {inet:ip_address(), inet:port_number()}},
 * , {peercert, nossl | [{cn, string()}, {dn, string()}]}
 * ]).
 */
public class EmqxConnectionInfo {
    //conn pid
    private long connId;
    private EmqxSocketType socketType;
    private String socketIP;
    private int socketPort;
    private String peerNameIp;
    private int peerNamePort;
    private String cert;
    private String cert_cn;
    private String cert_dn;

    //connInfo=[Tuple{elements=[Atom{value='socktype'},Atom{value='tcp'}]},
    // Tuple{elements=[Atom{value='peername'},Tuple{elements=[Tuple{elements=[127, 0, 0, 1]}, -9461]}]},
    // Tuple{elements=[Atom{value='sockname'},Tuple{elements=[Tuple{elements=[127, 0, 0, 1]}, 7993]}]},
    // Tuple{elements=[Atom{value='peercert'}, Atom{value='nossl'}]}]
    public static EmqxConnectionInfo praser(Pid connPid, Object connInfo) {
        EmqxConnectionInfo connectionInfo = new EmqxConnectionInfo();
        connectionInfo.setConnId(connPid.id);
        ArrayList<Object> connInfoList = (ArrayList<Object>) connInfo;
        try {
            for (Object o : connInfoList) {
                if (o instanceof Tuple) {
                    Tuple tupleElement = (Tuple) o;
                    Atom key = (Atom) tupleElement.get(0);
                    switch (key.value) {
                        case "socktype":
                            Atom socketTypeAtom = (Atom) tupleElement.get(1);
                            connectionInfo.setSocketType(EmqxSocketType.valueOf(socketTypeAtom.value));
                            break;
                        case "peername":
                            Tuple peernameTupleOutSide = (Tuple) tupleElement.get(1);
                            Tuple peernameTupleInSide = (Tuple) peernameTupleOutSide.get(0);
                            StringBuilder peerNameBuilder = new StringBuilder();
                            for (Integer i = 0; i < peernameTupleInSide.length(); i++) {
                                peerNameBuilder.append(peernameTupleInSide.get(i));
                                if(i != peernameTupleInSide.length()-1){
                                    peerNameBuilder.append(".");
                                }
                            }
                            int peername_Port = (int) peernameTupleOutSide.get(1);
                            connectionInfo.setPeerNameIp(peerNameBuilder.toString());
                            connectionInfo.setPeerNamePort(peername_Port);
                            break;
                        case "sockname":
                            Tuple socketnameTupleOutSide = (Tuple) tupleElement.get(1);
                            Tuple socketnameTupleInSide = (Tuple) socketnameTupleOutSide.get(0);
                            StringBuilder socketIpBuilder = new StringBuilder();
                            for (Integer i = 0; i < socketnameTupleInSide.length(); i++) {
                                socketIpBuilder.append(socketnameTupleInSide.get(i));
                                if(i != socketnameTupleInSide.length()-1){
                                    socketIpBuilder.append(".");
                                }
                            }
                            int socketname_Port = (int) socketnameTupleOutSide.get(1);
                            connectionInfo.setSocketIP(socketIpBuilder.toString());
                            connectionInfo.setSocketPort(socketname_Port);
                            break;
                        case "peercert":
                            Object certKeyObj = tupleElement.get(1);
                            if (certKeyObj instanceof Atom) {
                                connectionInfo.setCert("nossl");
                            }
                            if (certKeyObj instanceof ArrayList) {
                                ArrayList<Object> cert_list = (ArrayList<Object>) certKeyObj;
                                Tuple cert_cn_Tuple = (Tuple) cert_list.get(0);
                                Tuple cert_dn_Tuple = (Tuple) cert_list.get(0);
                                connectionInfo.setCert_cn(cert_cn_Tuple.get(1).toString());
                                connectionInfo.setCert_dn(cert_dn_Tuple.get(1).toString());
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

    public long getConnId() {
        return connId;
    }

    public void setConnId(long connId) {
        this.connId = connId;
    }

    public EmqxConnectionInfo() {
    }

    public EmqxSocketType getSocketType() {
        return socketType;
    }

    public void setSocketType(EmqxSocketType socketType) {
        this.socketType = socketType;
    }

    public String getSocketIP() {
        return socketIP;
    }

    public void setSocketIP(String socketIP) {
        this.socketIP = socketIP;
    }

    public String getPeerNameIp() {
        return peerNameIp;
    }

    public void setPeerNameIp(String peerNameIp) {
        this.peerNameIp = peerNameIp;
    }

    public int getPeerNamePort() {
        return peerNamePort;
    }

    public void setPeerNamePort(int peerNamePort) {
        this.peerNamePort = peerNamePort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getCert_cn() {
        return cert_cn;
    }

    public void setCert_cn(String cert_cn) {
        this.cert_cn = cert_cn;
    }

    public String getCert_dn() {
        return cert_dn;
    }

    public void setCert_dn(String cert_dn) {
        this.cert_dn = cert_dn;
    }

    @Override
    public String toString() {
        return "EmqxConnectionInfo{" +
                "connId=" + connId +
                ", socketType=" + socketType +
                ", socketIP='" + socketIP + '\'' +
                ", socketPort=" + socketPort +
                ", peerNameIp='" + peerNameIp + '\'' +
                ", peerNamePort=" + peerNamePort +
                ", cert='" + cert + '\'' +
                ", cert_cn='" + cert_cn + '\'' +
                ", cert_dn='" + cert_dn + '\'' +
                '}';
    }
}
