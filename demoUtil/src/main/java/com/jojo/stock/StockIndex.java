package com.jojo.stock;


public class StockIndex {

    private String name;
    private String code;
    private String valuablePoint;
    private String littleSpportPoint;
    private String supportPoint;


    public StockIndex() {
    }

    public StockIndex(String name, String code, String supportPoint) {
        this.name = name;
        this.code = code;
        this.supportPoint = supportPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSupportPoint() {
        return supportPoint;
    }

    public void setSupportPoint(String supportPoint) {
        this.supportPoint = supportPoint;
    }
}
