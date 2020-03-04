package com.boray.entity;

public class ProjectFileInfo {
    private String djname;//灯具名称
    private String djtype;//型号
    private String dmxstr;//DMX起始地址
    private String gcinfoid;//工程id
    private String zytd;//占用通道

    public String getDjtype() {
        return djtype;
    }

    public void setDjtype(String djtype) {
        this.djtype = djtype;
    }

    public String getDmxstr() {
        return dmxstr;
    }

    public void setDmxstr(String dmxstr) {
        this.dmxstr = dmxstr;
    }

    public String getGcinfoid() {
        return gcinfoid;
    }

    public void setGcinfoid(String gcinfoid) {
        this.gcinfoid = gcinfoid;
    }

    public String getZytd() {
        return zytd;
    }

    public void setZytd(String zytd) {
        this.zytd = zytd;
    }

    public String getDjname() {
        return djname;
    }

    public void setDjname(String djname) {
        this.djname = djname;
    }
}
