package com.jojo.leetcode.zz;

import lombok.Data;

@Data
public class MazePosition {

    private int x;
    private int y;

    public MazePosition() {
    }

    public MazePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static boolean equals(MazePosition posA, MazePosition posB) {
        if (posA == null || posB == null) {
            return false;
        }
        return equals(posA, posB.getX(), posB.getY());
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


}
