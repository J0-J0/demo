package com.jojo.leetcode.maze;


import java.util.ArrayList;
import java.util.List;

public class MazePosition {

    private int x;// 二维数组的里
    private int y;// 二维数组的外

    public MazePosition() {
    }

    public MazePosition(String posName) {
        char[] chars = posName.toCharArray();
        char y = chars[0];
        this.y = y - 97;

        char x = chars[1];
        this.x = x - 1 - 48;
    }

    public static List<MazePosition> buildList(String... arr) {
        List<MazePosition> positionList = new ArrayList<>();
        for (String s : arr) {
            positionList.add(new MazePosition(s));
        }
        return positionList;
    }

    public MazePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static boolean equals(MazePosition posA, MazePosition posB) {
        if (posA == null || posB == null) {
            return false;
        }
        return equals(posA, posB.getX(), posB.getY());
    }

    public static boolean equalsAny(MazePosition posA, List<MazePosition> posList) {
        boolean result = false;
        for (MazePosition mazePosition : posList) {
            result |= equals(posA, mazePosition);
        }
        return result;
    }

    public static boolean equals(MazePosition posA, int posBX, int posBY) {
        if (posA == null) {
            return false;
        }

        if (posA.getX() == posBX && posA.getY() == posBY) {
            return true;
        }
        return false;
    }

    @Override
    protected MazePosition clone() {
        MazePosition newPos = new MazePosition();
        newPos.setX(x);
        newPos.setY(y);
        return newPos;
    }

    @Override
    public String toString() {
        return (char) (y + 97) + "" + (x + 1);
    }

}
