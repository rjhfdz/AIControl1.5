package com.boray.entity;

public class RDM implements Comparable<RDM> {

    private String UID;
    private String model;
    private int DMXStart;
    private int aisle;
    private byte[] uidByte;
    private String uidTemp;

    public byte[] getUidByte() {
        return uidByte;
    }

    public void setUidByte(byte[] uidByte) {
        this.uidByte = uidByte;
    }

    public String getUidTemp() {
        return uidTemp;
    }

    public void setUidTemp(String uidTemp) {
        this.uidTemp = uidTemp;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getDMXStart() {
        return DMXStart;
    }

    public void setDMXStart(int DMXStart) {
        this.DMXStart = DMXStart;
    }

    public int getAisle() {
        return aisle;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    @Override
    public int compareTo(RDM o) {
        return this.getDMXStart() - o.getDMXStart();
    }
}
