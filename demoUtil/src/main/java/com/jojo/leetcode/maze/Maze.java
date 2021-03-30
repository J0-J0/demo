package com.jojo.leetcode.maze;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Maze {

    private String[][] maze;

    private Random mazeRand = new Random(105);
    public static final int default_size = 8;

    private List<MazePosition> fastOrSlowPositionList;

    private List<MazePosition> wallPositionList;

    private List<MazePosition> refreshPositionList = Lists.newArrayList();// 每一回合后，需要刷新的老位置

    private List<Cat> catList;

    public void setFastOrSlowPositionList(List<MazePosition> fastOrSlowPositionList) {
        this.fastOrSlowPositionList = fastOrSlowPositionList;
    }

    public void setWallPositionList(List<MazePosition> wallPositionList) {
        this.wallPositionList = wallPositionList;
    }

    public List<MazePosition> getFastOrSlowPositionList() {
        return fastOrSlowPositionList;
    }

    private void setPositionInMaze(MazePosition pos, String val) {
        String padVal = StringUtils.leftPad(val, default_size, " ");
        maze[pos.getX()][pos.getY()] = padVal;
    }

    private void setPositionInMaze(List<MazePosition> posList, String val) {
        if (CollectionUtils.isEmpty(posList)) {
            return;
        }
        for (MazePosition pos : posList) {
            setPositionInMaze(pos, val);
        }
    }

    public void initialize(int width, int height) {
        // 初始化迷宫 8 * 8
        maze = new String[width][height];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = StringUtils.leftPad("0", default_size, " ");
            }
        }

        // 初始化随机速度区域
        setPositionInMaze(fastOrSlowPositionList, "?");

        // 初始化墙
        setPositionInMaze(wallPositionList, "墙");

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
        position.setX(mazeRand.nextInt(maxX));// 坐标从0开始
        position.setY(mazeRand.nextInt(maxY));// 坐标从0开始
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

        setPositionInMaze(catPosition, postionStr);
    }


    public void print() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }


    public boolean addRefreshList(MazePosition position) {
        return refreshPositionList.add(position);
    }

    public boolean isMoveable(MazePosition newPos) {
        return isMoveable(newPos.getX(), newPos.getY());
    }

    public boolean isMoveable(int x, int y) {
        // 有没有碰撞
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

    public void refresh() {
        if (CollectionUtils.isEmpty(refreshPositionList)) {
            return;
        }

        for (MazePosition position : refreshPositionList) {
            if (MazePosition.equalsAny(position, fastOrSlowPositionList)) {
                this.setPositionInMaze(position, "?");
            } else {
                this.setPositionInMaze(position, "0");
            }
        }

        refreshPositionList.clear();
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

                System.out.println("随机落点");

                addRefreshList(cat.getPosition());
                cat.setPosition(this.randomPosition());
                System.out.println(cat.getName() + "新落点" + cat.getPosition().toString());

                addRefreshList(compareCat.getPosition());
                compareCat.setPosition(this.randomPosition());
                System.out.println(compareCat.getName() + "新落点" + compareCat.getPosition().toString());

                this.doRegisterCat(cat);
                this.doRegisterCat(compareCat);
                break;
            }
        }
    }

    /**
     * 返回最终的运动轨迹
     *
     * @param catNowPos
     * @param plannedPos
     * @param moveDesc
     * @return
     */
    public MazePosition getActuallyPos(MazePosition catNowPos, MazePosition plannedPos, StringBuffer moveDesc) {
        // 先把边界问题排除了
        int plannedPosX = plannedPos.getX(), plannedPosY = plannedPos.getY();
        if (plannedPosX < 0) {
            plannedPos.setX(0);
        }
        if (plannedPosX >= maze.length) {
            plannedPos.setX(maze.length - 1);
        }
        if (plannedPosY < 0) {
            plannedPos.setY(0);
        }
        if (plannedPosY >= maze[0].length) {
            plannedPos.setY(maze[0].length - 1);
        }

        // 移动一定在一个方向
        // x方向，只移动Y
        int catNowPosX = catNowPos.getX(), catNowPosY = catNowPos.getY();
        if (catNowPosX == plannedPosX) {
            if (catNowPosY < plannedPosY) {
                for (int i = catNowPosY + 1; i <= plannedPosY; i++) {
                    if (isMoveable(catNowPosX, i)) {
                        catNowPos.setY(i);
                        moveDesc.append("->" + catNowPos.toString());
                    } else {
                        moveDesc.append("撞墙，结束移动");
                        return catNowPos;
                    }
                }
            } else {
                for (int i = catNowPosY - 1; i >= plannedPosY; i--) {
                    if (isMoveable(catNowPosX, i)) {
                        catNowPos.setY(i);
                        moveDesc.append("->" + catNowPos.toString());
                    } else {
                        moveDesc.append("撞墙，结束移动");
                        return catNowPos;
                    }
                }
            }

            return catNowPos;
        }

        // y方向，只移动X
        if (catNowPosX < plannedPosX) {
            for (int i = catNowPosX + 1; i <= plannedPosX; i++) {
                if (isMoveable(i, catNowPosY)) {
                    catNowPos.setX(i);
                    moveDesc.append("->" + catNowPos.toString());
                } else {
                    moveDesc.append("撞墙，结束移动");
                    return catNowPos;
                }
            }
        } else {
            for (int i = catNowPosX - 1; i >= plannedPosX; i--) {
                if (isMoveable(i, catNowPosY)) {
                    catNowPos.setX(i);
                    moveDesc.append("->" + catNowPos.toString());
                } else {
                    moveDesc.append("撞墙，结束移动");
                    return catNowPos;
                }
            }
        }
        return catNowPos;
    }

    public boolean isCorner(MazePosition position) {
        if (!isMoveable(position.getX() - 1, position.getY()) && isMoveable(position.getX(), position.getY() - 1)) {
            return true;
        } else if (!isMoveable(position.getX() + 1, position.getY()) && isMoveable(position.getX(), position.getY() + 1)) {
            return true;
        } else if (!isMoveable(position.getX() + 1, position.getY()) && isMoveable(position.getX(), position.getY() - 1)) {
            return true;
        } else if (!isMoveable(position.getX() - 1, position.getY()) && isMoveable(position.getX(), position.getY() + 1)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isForkPosition(MazePosition position) {
        // todo 获取坐标周围点的数据，动态判断
        return false;
    }
}
