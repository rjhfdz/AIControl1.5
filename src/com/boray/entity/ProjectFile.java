package com.boray.entity;

public class ProjectFile {
    private int id;//ID
    private String gcname;//��������
    private String xmid;//��Ŀid
    private String createby;//������
    private String gcurl;//����·��

    private String str;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGcname() {
        return gcname;
    }

    public void setGcname(String gcname) {
        this.gcname = gcname;
    }

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getGcurl() {
        return gcurl;
    }

    public void setGcurl(String gcurl) {
        this.gcurl = gcurl;
    }
}
