
package com.jojo.zzz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public int orchestraLayout(int n, int xPos, int yPos) {
        //一共几圈
        int quan = (n + 1) / 2;
        long num = n;
        //第几圈
        int layer = Math.min(Math.min(yPos, xPos), Math.min(n - xPos - 1, n - yPos - 1)) + 1;
        //总面积
        long area = num * num;
        //当前所在圈面积
        long zhong = (num - 2 * (layer - 1));
        zhong *= zhong;
        //求差 +1 得到当前圈左上角编号
        long index = (area - zhong) % 9 + 1;
        //右边界
        int right = n - layer;
        //左边界
        int left = layer - 1;
        if (xPos == left) {
            //在 --- 上
            index += yPos - left;
        } else if (yPos == right) {
            //在   |上
            index += right - left;
            index += xPos - left;
        } else if (xPos == right) {
            //在 __ 上
            index += 2 * (right - left);
            index += right - yPos;
        } else {
            //在 |  上
            index += 3 * (right - left);
            index += right - xPos;
        }
        return (int) (index % 9 == 0 ? 9 : index % 9);
    }

    public static void main(String[] args) throws Exception {
        Work work = new Work();
        System.out.println(work.orchestraLayout(971131546, 966980466, 531910024));



    }


}