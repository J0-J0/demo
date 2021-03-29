package com.jojo.leetcode.zz;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.jcajce.provider.symmetric.SEED;

import java.util.Random;

@Data
public class Cat {
    public static final String type_catch = "抓";
    public static final String type_escape = "逃";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Random catRandom;// 随机方向器

    private String name;

    private MazePosition position;

    private String type;

    private int speed = 2;

    public Cat(String name, MazePosition position, String type, int randSeed) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.catRandom = new Random(randSeed);
    }

    public String getName() {
        return name + "-" + type;
    }

    public void move(Scence scence) {
        // todo 这里也存在bug，判断为不能动，不是不动，而是停在墙面前
        int direction = catRandom.nextInt(4);
        switch (direction) {
            case 0://上
                this.doMove(scence, new MazePosition(position.getX() + speed, position.getY()));
                break;

            case 1://下
                this.doMove(scence, new MazePosition(position.getX() - speed, position.getY()));
                break;

            case 2://左
                this.doMove(scence, new MazePosition(position.getX(), position.getY() - speed));
                break;

            case 3://右
                this.doMove(scence, new MazePosition(position.getX(), position.getY() + speed));
                break;

        }
    }

    private void doMove(Scence scence, MazePosition newPos) {
        boolean moveable = scence.isMoveable(newPos);// 能不能动
        if (!moveable) {
            System.out.println(this.getName() + " 撞墙，本次不移动");
            return;
        }

        System.out.println(this.getName() + " 从" + position.toString() + "移动到" + newPos.toString());

        scence.addRecoverList(position.clone());// 不能设置原始坐标对象
        position = newPos;
        scence.doRegisterCat(this);// 更新位置

        if (MazePosition.equals(scence.getFastOrSlowPosition(), newPos)) {
            int nextInt = catRandom.nextInt(2);
            if (nextInt == 0) {
                speed = 1;
            } else {
                speed = 4;
            }
        }
    }
}
