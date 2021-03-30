package com.jojo.leetcode.maze;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 给定一个 8 * 8的迷宫；
 * 3只猫，2只追、1只逃，初始速度2。猫的初始方向随机、游戏开始后，只准前进不准后退（当且仅当身份互换时，才能后退），遇到角落或是三岔路口，可以随机前进、左转或右转；
 * 存在2个区域，可能加速、可能减速。加速速度 x2、减速速度 /2，速度最高4，最慢1；
 * 存在很多道墙，不准移动；
 * 抓到后，互换身份，继续游戏；
 * 假设猫A负责追、猫B负责逃，那么猫A追到猫B后，猫A反转方向（后退），猫B继续前进方向。
 *
 * 用代码模拟100秒的运行情况;
 * 能用最短路径追到猫更好;
 *
 */
public class Game {

    private Maze maze;
    private List<Cat> catList;

    public void initialize() {
        maze = new Maze();
        maze.setFastOrSlowPositionList(MazePosition.buildList("a3", "f5"));
        maze.setWallPositionList(MazePosition.buildList("b2", "b3", "b4", "c2", "e2", "f2", "g2", "g3", "d4", "e4", "d5", "e5", "b6", "b7", "c7", "d7", "g5", "g6", "g7", "f7"));
        maze.initialize(Maze.default_size, Maze.default_size);

        catList = Lists.newArrayList(new Cat("猫a", new MazePosition("c1"), Cat.type_catch, 1));
        catList.add(new Cat("猫b", new MazePosition("c6"), Cat.type_escape, 2));
        catList.add(new Cat("猫c", new MazePosition("g8"), Cat.type_catch, 3));

        maze.registerCat(catList);
    }

    public void start(int round) {
        maze.print();
        for (int i = 0; i < round; i++) {
            StringBuffer moveDesc = new StringBuffer();
            moveDesc.append("===回合" + i);

            for (Cat cat : catList) {
                String oneCatMoveDesc = cat.move(maze);
                moveDesc.append("; "+oneCatMoveDesc);
            }
            System.out.println(moveDesc.toString().replaceFirst(";", ""));

            maze.catchOrNot();

//            System.out.println("移动完毕，此时战场");
//            maze.print();
            maze.refresh();// 打扫战场

//            maze.print();
//            System.out.println();
//            System.out.println();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();

        game.initialize();
        game.start(100);
    }

}
