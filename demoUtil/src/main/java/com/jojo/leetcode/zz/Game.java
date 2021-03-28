package com.jojo.leetcode.zz;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 给定一个 8 * 8的迷宫；
 * 3只猫，2只追、1只逃，初始速度2；
 * 存在一个区域，可能加速、可能减速。加速一次走4格、减速一次走1格；
 * 抓到后，互换身份，并继续游戏
 */
@Data
public class Game {

    private Scence scence;
    private List<Cat> catList;

    public void initialize() {
        scence = new Scence();
        scence.initialize();

        catList = Lists.newArrayList(new Cat("猫a", scence.randomPosition(), Cat.type_escape, 1));
        catList.add(new Cat("猫b", scence.randomPosition(), Cat.type_catch, 2));
        catList.add(new Cat("猫c", scence.randomPosition(), Cat.type_catch, 3));

        scence.registerCat(catList);
    }

    public void start(int round) {
        scence.print();
        for (int i = 0; i < round; i++) {
            for (Cat cat : catList) {
                cat.move(scence);
            }
            scence.catchOrNot();
//            System.out.println("移动完毕，此时战场");
//            scence.print();
            scence.recover();// 打扫战场

            System.out.println("===回合" + i);
            scence.print();
            System.out.println();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();

        game.initialize();
        game.start(100);
    }

}
