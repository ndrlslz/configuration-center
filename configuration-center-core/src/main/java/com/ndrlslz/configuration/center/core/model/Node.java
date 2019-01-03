package com.ndrlslz.configuration.center.core.model;

import org.apache.zookeeper.data.Stat;

public class Node {
    private String name;
    private String value;
    private long czxid;
    private long mzxid;
    private long ctime;
    private long mtime;
    private int version;
    private int cversion;
    private int aversion;
    private long ephemeralOwner;
    private int dataLength;
    private int numChildren;
    private long pzxid;

    private Node(Builder builder) {
        this.value = builder.value;
        this.czxid = builder.stat.getCzxid();
        this.mzxid = builder.stat.getMzxid();
        this.ctime = builder.stat.getCtime();
        this.mtime = builder.stat.getMtime();
        this.version = builder.stat.getVersion();
        this.cversion = builder.stat.getCversion();
        this.aversion = builder.stat.getAversion();
        this.ephemeralOwner = builder.stat.getEphemeralOwner();
        this.dataLength = builder.stat.getDataLength();
        this.numChildren = builder.stat.getNumChildren();
        this.pzxid = builder.stat.getPzxid();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public long getCzxid() {
        return czxid;
    }

    public long getMzxid() {
        return mzxid;
    }

    public long getCtime() {
        return ctime;
    }

    public long getMtime() {
        return mtime;
    }

    public int getVersion() {
        return version;
    }

    public int getCversion() {
        return cversion;
    }

    public int getAversion() {
        return aversion;
    }

    public long getEphemeralOwner() {
        return ephemeralOwner;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public long getPzxid() {
        return pzxid;
    }

    public static class Builder {
        private String value;
        private Stat stat;

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withStat(Stat stat) {
            this.stat = stat;
            return this;
        }

        public Node build() {
            return new Node(this);
        }
    }
}
