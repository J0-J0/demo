package com.jojo.util.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class EDaIndex {

    private static List<StockIndex> indexList = new ArrayList<>();

    static {
        indexList.add(new StockIndex("上证50", "sh000001", "2800"));
        indexList.add(new StockIndex("沪深300", "sh000300", "3500"));
        indexList.add(new StockIndex("中证500", "sh000905", "4800"));
        indexList.add(new StockIndex("全指医药", "sh000991", "9700"));
        indexList.add(new StockIndex("中证医疗", "sz399989", "7900"));
        indexList.add(new StockIndex("全职信息", "sh000993", "4600"));
        indexList.add(new StockIndex("全职消费", "sh000990", "11700"));
        indexList.add(new StockIndex("中证1000", "sh000852", "5880"));
        indexList.add(new StockIndex("中证军工", "sz399967", "8300"));
        indexList.add(new StockIndex("恒生", "hkHSI", "18800"));
        indexList.add(new StockIndex("恒生医疗", "hkHSHCI", "2900"));
        indexList.add(new StockIndex("创业板", "sz399006", "1680"));
        indexList.add(new StockIndex("中证传媒", "sz399971", "931"));
        indexList.add(new StockIndex("内地消费", "sh000942", "7700"));
        indexList.add(new StockIndex("证券公司", "sz399975", "600"));
    }


    private static final String tencet_api_url = "https://qt.gtimg.cn/q=";

    public static List<StockIndex> getIndexList() {
        return indexList;
    }

    public static List<String> analyse() {
        List<String> situationList = new ArrayList<>();
        for (StockIndex stockIndex : indexList) {
            HttpResponse httpResponse = HttpUtil.createGet(tencet_api_url + stockIndex.getCode()).execute();
            String body = httpResponse.body();
            String[] tencetArr = body.split("~");
            if (tencetArr.length < 4) {
                System.out.println(stockIndex.getName() + "接口异常，退出");
                continue;
            }

            String currentPoint = tencetArr[3];
            BigDecimal supportPoint = new BigDecimal(stockIndex.getSupportPoint());
            BigDecimal difference = supportPoint.subtract(new BigDecimal(currentPoint));
            int compareVal = difference.compareTo(BigDecimal.ZERO);

            DecimalFormat decimalFormat = new DecimalFormat("0.00%");
            if (compareVal < 0) {
                // 高于支撑位
                String percent = decimalFormat.format(difference.abs().divide(supportPoint, RoundingMode.CEILING));
                situationList.add(stockIndex.getName() + "当前点位【" + currentPoint + "】距离支撑点位【" + stockIndex.getSupportPoint() + "】，还需要跌" + percent);
            } else {
                // 低于支撑位
                String percent = decimalFormat.format(difference.divide(supportPoint, RoundingMode.CEILING));
                situationList.add(stockIndex.getName() + "当前点位【" + currentPoint + "】已经低于支撑点位【" + stockIndex.getSupportPoint() + "】，跌幅已达" + percent);
            }
        }
        return situationList;
    }

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("1","2","3");
        String two = "2";
        List<String> collect = list.stream().filter(two::equals).collect(Collectors.toList());

        System.out.println(collect);

        System.out.println(list.stream().filter(two::equals).count());
    }
}
