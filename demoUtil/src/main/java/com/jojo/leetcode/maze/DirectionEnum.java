package com.jojo.leetcode.maze;

public enum DirectionEnum {
    UP(0, "上"),
    DOWN(1, "下"),
    LEFT(2, "左"),
    RIGHT(3, "右"),

    ;

    private int direction;
    private String directionDesc;

    DirectionEnum(int direction, String directionDesc) {
        this.direction = direction;
        this.directionDesc = directionDesc;
    }

    public int getDirection() {
        return direction;
    }
}
