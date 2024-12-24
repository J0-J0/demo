package com.jojo.util.lottery;

import java.util.Random;

/**
 * <p>
 * 彩票这种东西，就是把大部分人的钱丢给小部分人，但与一般赌博不同，彩票没有任何技巧可言，<br/>
 * 显得非常文雅。
 * </p>
 * <p>
 * 彩票的规则相当简单，大部分都是给“号码”下注，“号码”是一串数字，但有一定的规则，以<br/>
 * 福彩双色球为例，7个不超过33的两位数，”05 10 17 19 29 32 12“。假设你选中了这个号码，<br/>
 * 那么就可以花上几块钱去给这个号码下注，而你投入的资金会流向一个叫做“奖池”的地方，<br/>
 * 所有人的下注的钱都会流向这个地方，买的人越多，池子里的钱就越多。每周二、四、日，官方<br/>
 * 会随机生成7个不超过33的两位数，如果你下注的号码与官方随机生成的号码一致，恭喜，可以<br/>
 * 爱干嘛干嘛去了，那是至少500W的奖金。
 * </p>
 * <p>
 * 那彩票机构要怎么赚钱呢？事实上，彩票机构不会把奖池里的奖金统统赏给押中号码的人，它们<br/>
 * 按照某种比例留一部分，那些钱就是机构的。所以，办彩票是稳赚不赔的买卖，只要有一个人买，那么<br/>
 * 机构就有钱入账，聚沙成塔，最后的利润尤为可观。
 * 
 * @author jojo
 *
 */
public class ChinaWelfareLottery {

	/**
	 * 双色球
	 * 
	 * @return
	 */
	public static String colorBall() {
		// 红球种子
		int[] redSeed = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
				27, 28, 29, 30, 31, 32, 33 };
		// 蓝球种子
		int[] blueSeed = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();

		// 获取红球号码
		stringBuffer.append("红球：");
		for (int i = 0; i < 6; i++) {
			stringBuffer.append(redSeed[Math.abs(random.nextInt()) % redSeed.length]);
			stringBuffer.append(" ");
		}
		// 获取蓝球号码
		stringBuffer.append("蓝球：");
		stringBuffer.append(blueSeed[Math.abs(random.nextInt()) % blueSeed.length]);

		return stringBuffer.toString();
	}

	public static void main(String[] args) {
		System.out.println(colorBall());
	}
}
