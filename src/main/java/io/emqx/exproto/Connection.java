package io.emqx.exproto;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Pid;

public class Connection {
    private Pid pid;

    public Atom node;
    public Long id;
    public Long serial;
    public Long creation;

    public Connection(Pid pid) {
        this.pid=pid;
        this.node = pid.node;
        this.id = pid.id;
        this.serial = pid.serial;
        this.creation = pid.creation;
    }

    public Connection(Atom node, Long id, Long serial, Long creation) {
        this.node = node;
        this.id = id;
        this.serial = serial;
        this.creation = creation;
    }

    public Pid getPid() {
        return pid;
    }

    public void setPid(Pid pid) {
        this.pid = pid;
    }

    public Atom getNode() {
        return node;
    }

    public void setNode(Atom node) {
        this.node = node;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerial() {
        return serial;
    }

    public void setSerial(Long serial) {
        this.serial = serial;
    }

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "node=" + node +
                ", id=" + id +
                ", serial=" + serial +
                ", creation=" + creation +
                '}';
    }
}
