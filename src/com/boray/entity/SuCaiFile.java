package com.boray.entity;

public class SuCaiFile {
    private String filename;
    private Integer id;
    private String kuname;
    private String sctype;
    private String shucaifile;
    private String shucainame;
    private String countsctype;

    public String getShucainame() {
        return shucainame;
    }

    public void setShucainame(String shucainame) {
        this.shucainame = shucainame;
    }

    public String getCountsctype() {
        return countsctype;
    }

    public void setCountsctype(String countsctype) {
        this.countsctype = countsctype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKuname() {
        return kuname;
    }

    public void setKuname(String kuname) {
        this.kuname = kuname;
    }

    public String getSctype() {
        return sctype;
    }

    public void setSctype(String sctype) {
        this.sctype = sctype;
    }

    public String getShucaifile() {
        return shucaifile;
    }

    public void setShucaifile(String shucaifile) {
        this.shucaifile = shucaifile;
    }
}
