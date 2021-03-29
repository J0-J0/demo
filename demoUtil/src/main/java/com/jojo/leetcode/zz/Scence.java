package com.jojo.leetcode.zz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Scence {

    private String[][] maze;

    private Random scenceRand = new Random(105);
    private static final int default_size = 8;

    @Getter
    private MazePosition fastOrSlowPosition;

    private List<MazePosition> wallPositionList;

    private List<MazePosition> recoverPositionList = Lists.newArrayList();// 每一回合后，需要刷新的老位置

    private List<Cat> catList;

    private void setPositionInScence(MazePosition pos, String val) {

        String padVal = StringUtils.leftPad(val, default_size, " ");
        maze[pos.getX()][pos.getY()] = padVal;
        String mazeVal = maze[pos.getX()][pos.getY()];
    }

    private void setPositionInScence(List<MazePosition> posList, String val) {
        if (CollectionUtils.isEmpty(posList)) {
            return;
        }
        for (MazePosition pos : posList) {
            setPositionInScence(pos, val);
        }
    }

    public void initialize() {
        // 初始化迷宫 8 * 8
        maze = new String[default_size][default_size];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = StringUtils.leftPad("0", default_size, " ");
            }
        }

        // 初始化随机速度区域
        fastOrSlowPosition = this.randomPosition();
        setPositionInScence(fastOrSlowPosition, "加速/减速");

        // 初始化墙
        wallPositionList = null;
        setPositionInScence(wallPositionList, "墙");

    }

    public MazePosition randomPosition() {
        int maxX = maze.length - 1, maxY = maze[0].length - 1;

        // todo 这里也有bug，要避开墙和加速区域
//        List<Integer> xNotInList = Lists.newArrayList(), yNotInList = Lists.newArrayList();
//        if (fastOrSlowPosition != null) {
//            xNotInList.add(fastOrSlowPosition.getX());
//            yNotInList.add(fastOrSlowPosition.getY());
//        }
//        if (CollectionUtils.isNotEmpty(wallPositionList)) {
//
//        }

        MazePosition position = new MazePosition();
        position.setX(scenceRand.nextInt(maxX));// 坐标从0开始
        position.setY(scenceRand.nextInt(maxY));// 坐标从0开始
        return position;
    }

    public void registerCat(List<Cat> catList) {
        this.catList = catList;
        for (Cat cat : catList) {
            this.doRegisterCat(cat);
        }
    }

    public void doRegisterCat(Cat cat) {
        // 不能把加速区域更新没
        MazePosition catPosition = cat.getPosition();
        String postionStr = cat.getName();

        if (MazePosition.equals(catPosition, fastOrSlowPosition)) {
            postionStr += " 加速/减速";
        }
        setPositionInScence(catPosition, postionStr);
    }


    public void print() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }


    public boolean addRecoverList(MazePosition position) {
        return recoverPositionList.add(position);
    }

    public boolean isMoveable(MazePosition newPos) {
        return isMoveable(newPos.getX(), newPos.getY());
    }

    public boolean isMoveable(int x, int y) {
        // 有没有碰撞
        // todo 这里估计会有穿墙bug
        if (CollectionUtils.isNotEmpty(wallPositionList)) {
            for (MazePosition position : wallPositionList) {
                if (MazePosition.equals(position, x, y)) {
                    return false;
                }
            }
        }

        // 不能越界
        boolean isMoveable = (x < maze.length && x >= 0);
        isMoveable &= (y < maze[0].length && y >= 0);

        return isMoveable;
    }

    public void recover() {
        if (CollectionUtils.isEmpty(recoverPositionList)) {
            return;
        }

        for (MazePosition position : recoverPositionList) {
            if (MazePosition.equals(position, fastOrSlowPosition)) {
                this.setPositionInScence(position, "加速/减速");
            } else {
                this.setPositionInScence(position, "0");
            }
        }

        recoverPositionList.clear();
    }

    public void catchOrNot() {
        Map<String, Cat> catMap = Maps.newHashMap();

        for (Cat cat : catList) {
            String key = cat.getPosition().toString();
            Cat compareCat = catMap.get(key);
            if (compareCat == null) {
                catMap.put(key, cat);
                continue;
            }

            System.out.println(cat.getName() + "与" + compareCat.getName() + "碰撞");

            if (!StringUtils.equals(cat.getType(), compareCat.getType())) {
                System.out.println("身份互换");
                String tempType = new String(compareCat.getType());
                compareCat.setType(cat.getType());
                cat.setType(tempType);
            }

            System.out.println("随机落点");
            cat.setPosition(this.randomPosition());
            System.out.println(cat.getName() + "新落点" + cat.getPosition().toString());
            compareCat.setPosition(this.randomPosition());
            System.out.println(compareCat.getName() + "新落点" + compareCat.getPosition().toString());
            this.doRegisterCat(cat);
            this.doRegisterCat(compareCat);
            break;
        }
    }
}
