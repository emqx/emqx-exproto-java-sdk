package io.emqx.exproto;

public class Peercert {
    private String cert_cn;
    private String cert_dn;

    public Peercert(String cert_cn, String cert_dn) {
        this.cert_cn = cert_cn;
        this.cert_dn = cert_dn;
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
        return "Peercert{" +
                "cert_cn='" + cert_cn + '\'' +
                ", cert_dn='" + cert_dn + '\'' +
                '}';
    }
}
