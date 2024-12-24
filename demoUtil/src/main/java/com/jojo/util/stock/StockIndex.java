package com.jojo.util.stock;

import lombok.Data;

@Data
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

}
