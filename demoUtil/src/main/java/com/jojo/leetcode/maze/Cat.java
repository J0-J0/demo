package com.jojo.leetcode.maze;

import java.util.Random;

public class Cat {
    public static final String type_catch = "抓";
    public static final String type_escape = "逃";

    private Random catRandom;// 随机方向器

    private String name;

    private MazePosition position;

    private String type;

    private int speed = 2;

    private DirectionEnum catDirection;

    public Cat(String name, MazePosition position, String type, int randSeed) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.catRandom = new Random(randSeed);
    }

    public Cat(String name, MazePosition position, String type, int randSeed, DirectionEnum direction) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.catRandom = new Random(randSeed);
        this.catDirection = direction;
    }

    public MazePosition getPosition() {
        return position;
    }

    public void setPosition(MazePosition position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name + "-" + type;
    }

    public String move(Maze maze) {
        // todo 这里除了方向问题，还需要DFS
        String oneCatMoveDesc = "";
        int direction = catRandom.nextInt(4);
        switch (direction) {
            case 0://上
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX() + speed, position.getY()));
                break;

            case 1://下
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX() - speed, position.getY()));
                break;

            case 2://左
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX(), position.getY() - speed));
                break;

            case 3://右
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX(), position.getY() + speed));
                break;
        }

        return oneCatMoveDesc;
    }

    /**
     * 方向控制
     *
     * @param maze
     * @return
     */
    public String moveRefactor(Maze maze) {
        String oneCatMoveDesc = "";
        switch (catDirection) {
            case UP://上
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX() + speed, position.getY()));
                break;

            case DOWN://下
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX() - speed, position.getY()));
                break;

            case LEFT://左
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX(), position.getY() - speed));
                break;

            case RIGHT://右
                oneCatMoveDesc = this.doMove(maze, new MazePosition(position.getX(), position.getY() + speed));
                break;
        }

        return oneCatMoveDesc;
    }

    private String doMove(Maze maze, MazePosition plannedPos) {

        StringBuffer moveDesc = new StringBuffer(this.getName() + " 移动->" + position.toString());
        // 获取真实点位
        MazePosition actuallyPos = maze.getActuallyPos(position.clone(), plannedPos, moveDesc);

        plannedPos = actuallyPos;

        if (!MazePosition.equals(position, plannedPos)) {
            maze.addRefreshList(position.clone());
        }
        position = plannedPos;
        maze.doRegisterCat(this);// 更新位置

        if (MazePosition.equalsAny(plannedPos, maze.getFastOrSlowPositionList())) {
            int nextInt = catRandom.nextInt(2);
            if (nextInt == 0) {
                if (speed == 1) {
                    moveDesc.append("速度变为1格/秒");
                } else {
                    speed /= 2;
                    moveDesc.append("速度变为" + speed + "格/秒");
                }
            } else {
                if (speed == 4) {
                    moveDesc.append("速度变为4格/秒");
                } else {
                    speed *= 2;
                    moveDesc.append("速度变为" + speed + "格/秒");
                }
            }
        }

        // 输出
        return moveDesc.toString().replaceFirst("->", "");
    }
}
