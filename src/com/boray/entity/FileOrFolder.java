package com.boray.entity;

public class FileOrFolder {
    private String createby;//������
    private String createdate;//����ʱ��  ʱ���
    private Integer id;
    private String xmname;//��Ŀ��
    private Integer xmtype;//���� 0������ 1����˾

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXmname() {
        return xmname;
    }

    public void setXmname(String xmname) {
        this.xmname = xmname;
    }

    public Integer getXmtype() {
        return xmtype;
    }

    public void setXmtype(Integer xmtype) {
        this.xmtype = xmtype;
    }
}
