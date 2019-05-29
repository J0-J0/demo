package com.jojo.crawler;

import static com.jojo.util.http.HttpConstant.ATTR_href;
import static com.jojo.util.http.HttpConstant.ATTR_src;
import static com.jojo.util.http.HttpConstant.TAG_a;
import static com.jojo.util.http.HttpConstant.TAG_img;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.jojo.util.FileUtil;
import com.jojo.util.RegexUtil;
import com.jojo.util.SeleniumUtil;

/**
 * 开心就好
 * 
 * @author jojo
 *
 */
public class MangaCrawler {

	/**
	 * 代表一部漫画
	 */
	public static final String SHU_HUI_PREFIX_CARTOON = "http://www.ishuhui.com/cartoon/book";

	/**
	 * 一部漫画中的某一话
	 */
	public static final String SHU_HUI_PREFIX_CARTOON_NUM = "http://www.ishuhui.com/cartoon/num";

	/**
	 * 一部漫画中某一话的真实地址
	 */
	public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL = "http://hanhuazu.cc/cartoon/post";

	/**
	 * 一部漫画中某一话的真实地址
	 */
	public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL_2 = "http://www.ishuhui.com/cartoon/post";

	/**
	 * 图片地址
	 */
	public static final String SHU_HUI_PREFIX_PIC = "http://pic";

	/**
	 * feiwan 网图片地址url正则
	 */
	public static final String FEIWAN_PICURL_REGEX = "http(s?)://img.feiwan.net/[\\w\\d/]+\\.(jpg|png|jpeg)";

	/**
	 * 动漫之家图片地址
	 */
	public static final String DMZJ_PICURL_REGEX = "images.dmzj.com/y/[%_\\w/]+/\\d+\\.(jpg|png|jpeg)";

	/**
	 * 某一话及该话的URL存放地
	 */
	public static final String PATH_OF_TITLE_AND_URL = "D:\\Workspace\\test\\manga\\titleAndUrl.txt";

	public static final File FILE_OF_TITLE_AND_URL = new File(PATH_OF_TITLE_AND_URL);

	public static final String BASE_SAVE_DIRECTORY = "D:\\Workspace\\test\\manga\\";

	private static Logger logger = LoggerFactory.getLogger(MangaCrawler.class);

	/**
	 * 从文件中读标题与这一话的url
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getTitleAndUrlFromFile(String filepath) throws IOException {
		Map<String, String> titleAndUrl = Maps.newHashMap();
		File file = new File(filepath);
		BufferedReader reader = Files.newReader(file, Charset.forName("UTF-8"));
		String temp = null;
		while ((temp = reader.readLine()) != null) {
			if (StringUtils.isBlank(temp)) {
				continue;
			}
			String[] arr = temp.split(";");
			titleAndUrl.put(arr[0], arr[1]);
		}
		// try你卵子
		reader.close();
		return titleAndUrl;
	}

	/**
	 * 没有正则表达式，只是简单的用了下contains方法
	 * 
	 * @param url
	 * @param keyWord
	 * @throws IOException
	 */
	public static void saveTitleAndUrl(String url, String keyWord) throws IOException {
		// 前期准备
		File file = new File(PATH_OF_TITLE_AND_URL);
		if (!file.exists()) {
			Files.createParentDirs(file);
			file.createNewFile();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

		WebDriver driver = SeleniumUtil.getWebDriver();
		driver.get(url);
		// 获取所有A标签
		List<WebElement> webElements = driver.findElements(By.tagName(TAG_a));
		for (WebElement webElement : webElements) {
			String text = webElement.getText();
			if (StringUtils.contains(text, keyWord)) {
				String titleAndUrl = webElement.getText() + ";" + webElement.getAttribute(ATTR_href);
				logger.error("找到{}", titleAndUrl);
				bufferedWriter.write(titleAndUrl);
				bufferedWriter.write("\r\n");
			}
		}

		driver.close();
		bufferedWriter.close();
	}

	/**
	 * 保存至本地，文件名就用url里的
	 * 
	 * @param allPicUrlMap
	 * @throws Exception
	 */
	public static void saveToLocalWithOriginName(Map<String, List<String>> allPicUrlMap) throws Exception {
		for (Map.Entry<String, List<String>> entry : allPicUrlMap.entrySet()) {
			String directory = BASE_SAVE_DIRECTORY + FileUtil.filterInvalidCharacter(entry.getKey()) + File.separator;
			for (String picUrl : entry.getValue()) {
				String fileName = directory + RegexUtil.getLastPartOfUrl(picUrl);
				FileUtil.createNewFileFromInternet(picUrl, fileName);
			}
		}
	}

	/**
	 * 保存至本地，文件名不使用url里的，单纯按序命名
	 * 
	 * @param allPicUrlMap
	 * @throws Exception
	 */
	public static void saveToLocal(Map<String, List<String>> allPicUrlMap) throws Exception {
		for (Map.Entry<String, List<String>> entry : allPicUrlMap.entrySet()) {
			String directory = BASE_SAVE_DIRECTORY + FileUtil.filterInvalidCharacter(entry.getKey()) + File.separator;
			int i = 1;
			for (String picUrl : entry.getValue()) {
				// 后缀名不能丢额
				String fileName = directory + (i++) + "." + RegexUtil.getSuffixFromUrl(picUrl);
				FileUtil.createNewFileFromInternet(picUrl, fileName);
			}
		}
	}

	/**
	 * 取一部漫画中，每一话的地址
	 * 
	 * @param url
	 * @return
	 */
	public static List<String> getMangaUrlFromShuHui(String url) {
		WebDriver webDriver = SeleniumUtil.getWebDriver();
		webDriver.get(url);

		List<String> hrefList = Lists.newArrayList();
		List<WebElement> aTagList = webDriver.findElements(By.tagName(TAG_a));
		logger.error("共获取到{}个a标签，下面开始过滤", aTagList.size());
		for (WebElement a : aTagList) {
			String href = a.getAttribute("href");
			if (StringUtils.startsWith(href, SHU_HUI_PREFIX_CARTOON_NUM)) {
				logger.error("当前链接为：{} 符合条件", href);
				hrefList.add(href);
			} else {
				logger.error("当前链接为：{} 不符合条件，剔除", href);
			}
		}
		logger.error("过滤后的数量为{}", hrefList.size());

		webDriver.close();
		return hrefList;
	}

	/**
	 * 取某一话的真实地址
	 * 
	 * @param hrefList
	 * @return
	 */
	public static List<String> getRealMangaUrlFromShuHui(List<String> hrefList) {
		WebDriver webDriver = SeleniumUtil.getWebDriver();
		List<String> realMangaUrlList = Lists.newArrayList();
		for (String href : hrefList) {
			logger.error("开始访问{}", href);
			webDriver.get(href);
			List<WebElement> tempList = webDriver.findElements(By.tagName(TAG_a));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (WebElement temp : tempList) {
				String realUrl = temp.getAttribute("href");
				boolean flag = StringUtils.startsWith(realUrl, SHU_HUI_PREFIX_CARTOON_NUM_DETAIL);
				flag |= StringUtils.startsWith(realUrl, SHU_HUI_PREFIX_CARTOON_NUM_DETAIL_2);
				if (flag) {
					logger.error("找到真正的漫画url：{}", realUrl);
					realMangaUrlList.add(realUrl);
				}
			}
		}
		logger.error("真实漫画Url的个数：{}", realMangaUrlList.size());

		webDriver.close();
		return realMangaUrlList;
	}

	/**
	 * 取所有图片的url，同时返回某一话的标题，方便生成目录
	 * 
	 * @param realMangaUrlList
	 * @return
	 */
	public static Map<String, List<String>> getAllPicUrlFromShuHui(List<String> realMangaUrlList) {
		WebDriver webDriver = SeleniumUtil.getWebDriver();

		Map<String, List<String>> allPicUrlMap = Maps.newHashMap();
		for (String realMangaUrl : realMangaUrlList) {
			logger.error("准备访问url：{}", realMangaUrl);
			webDriver.get(realMangaUrl);

			List<WebElement> imgElementList = webDriver.findElements(By.tagName(TAG_img));
			List<String> srcList = Lists.newArrayList();
			for (WebElement img : imgElementList) {
				String src = img.getAttribute("src");
				// vue.js的懒加载，某些图片会放置在这个属性里，但这个属性是不会有http://前缀的
				String dataSrc = img.getAttribute("data-src");
				if (StringUtils.startsWith(src, SHU_HUI_PREFIX_PIC)) {
					logger.error("当前链接为：{} 符合条件", src);
					srcList.add(src);
				} else if (StringUtils.contains(dataSrc, "pic")) {
					dataSrc = "http:" + dataSrc;
					srcList.add(dataSrc);
					logger.error("当前链接为：{} 符合条件", dataSrc);
				} else {
					logger.error("此对象没有找到合适的图片地址：{}", img.toString());
				}
			}

			Document document = Jsoup.parse(webDriver.getPageSource());
			allPicUrlMap.put(document.title(), srcList);
		}

		webDriver.close();
		return allPicUrlMap;
	}

	/**
	 * 爬一整部漫画
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void downMangaFromShuHui(String url) throws Exception {
//		List<String> hrefList = getMangaUrlFromShuHui(url);

		// 20180906，存在一部分漫画，可以直接获取单话地址，不用在转一下
		List<String> hrefList = Lists.newArrayList(url);

		List<String> realMangaUrlList = getRealMangaUrlFromShuHui(hrefList);
		Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui(realMangaUrlList);
		saveToLocalWithOriginName(allPicUrlMap);
	}

	/**
	 * 爬取指定的某几话
	 * 
	 * @param no
	 * @throws Exception
	 */
	public static void downMangaFromShuHui(int... no) throws Exception {
		List<String> urlList = Lists.newArrayList();
		for (int i : no) {
			String url = "http://hanhuazu.cc/cartoon/post?id=" + i;
			urlList.add(url);
		}
		Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui(urlList);
		saveToLocalWithOriginName(allPicUrlMap);
	}

	/**
	 * 
	 * @param domainName 这个是域名，因为feiwan网会为某一部漫画直接设置一个域名。<br>
	 *                   url是restful风格，如果是漫画，那就域名后再跟一个/manhua<br>
	 *                   第几话就后面数字加.html<br>
	 * @return
	 */
	public static List<String> getMangaUrlFromFeiWan(String domainName) throws Exception {
		List<String> urlList = Lists.newArrayList();

		String url = domainName + "/xiangguan/";
		String mangaRegex = domainName + "/\\d+\\.html";

		Document document = Jsoup.parse(new URL(url), 10 * 1000);
		Elements elements = document.getElementsByTag(TAG_a);

		for (Element element : elements) {
			String string = element.attr(ATTR_href);
			if (string.matches(mangaRegex)) {
				urlList.add(string);
			}
		}
		return urlList;
	}

	/**
	 * 获取所有图片的url，feiwan网把所有的图片都放在<script>块内
	 * 
	 * @param mangaUrlList
	 * @return
	 */
	public static Map<String, List<String>> getAllPicUrlFromFeiWan(List<String> mangaUrlList) throws Exception {
		Map<String, List<String>> picUrlMap = Maps.newHashMap();

		for (String mangaUrl : mangaUrlList) {
			Document document = Jsoup.parse(new URL(mangaUrl), 10 * 1000);
			// 每一话的title
			String title = document.title();

			Pattern pattern = Pattern.compile(FEIWAN_PICURL_REGEX);
			Matcher matcher = pattern.matcher(document.toString());

			while (matcher.find()) {
				String picUrl = matcher.group();
				logger.error("找到：{}", picUrl);
				if (picUrlMap.get(title) == null) {
					List<String> picUrlList = Lists.newArrayList(picUrl);
					picUrlMap.put(title, picUrlList);
				} else {
					picUrlMap.get(title).add(picUrl);
				}
			}
		}

		return picUrlMap;
	}

	/**
	 * 
	 * @param domainName
	 * @throws Exception
	 */
	public static void downMangaFromFeiWan(String domainName) throws Exception {
		List<String> mangaUrlList = getMangaUrlFromFeiWan(domainName);
		Map<String, List<String>> allPicUrlMap = getAllPicUrlFromFeiWan(mangaUrlList);
		saveToLocalWithOriginName(allPicUrlMap);
	}

	/**
	 * 从动漫之家爬漫画
	 * 
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	public static void downMangaFromDMZJ() throws Exception {
		String filepath = "C:\\Users\\Administrator\\Desktop\\新建文本文档.txt";
		Map<String, String> titleAndUrl = getTitleAndUrlFromFile(filepath);

		Map<String, List<String>> picUrlMap = Maps.newHashMap();
		WebDriver webDriver = SeleniumUtil.getWebDriver();

		for (Entry<String, String> entry : titleAndUrl.entrySet()) {
			List<String> urlList = Lists.newArrayList();

			webDriver.get(entry.getValue());
			String html = webDriver.getPageSource();

			Pattern p = Pattern.compile(DMZJ_PICURL_REGEX);
			Matcher m = p.matcher(html);
			while (m.find()) {
				String str = m.group();
				System.out.println("找到目标" + str);
				urlList.add("https://" + str);
			}

			picUrlMap.put(entry.getKey(), urlList);
		}

		saveToLocalWithOriginName(picUrlMap);

	}

	/**
	 * 
	 * @param pageFlag 是否遍历分页
	 * @throws Exception
	 */
	public static void downMangaFromTieBa(boolean pageFlag) throws Exception {
		Map<String, String> titleAndUrl = getTitleAndUrlFromFile(PATH_OF_TITLE_AND_URL);

		Map<String, List<String>> picUrlMap = Maps.newHashMap();

		for (Entry<String, String> entry : titleAndUrl.entrySet()) {
			String url = entry.getValue();
			List<String> urlList = getPicUrlFromTieBa(url, pageFlag);
			picUrlMap.put(entry.getKey(), urlList);
		}

		saveToLocal(picUrlMap);
	}

	/**
	 * 贴吧有分页，这是比较麻烦的地方<br>
	 * 如果“没有获取到图片”的次数，与“本次获取的图片数量与上次获取的数量一致”<br>
	 * 的次数，相加超过某个数，那么，就判断读到末尾。<br>
	 * 另外这个方法不具备通用性，截止至目前为止，我都是靠class来获取图片的
	 * 
	 * @param url
	 * @param pageFlag 是否遍历分页
	 * @return
	 * @throws IOException
	 */
	public static List<String> getPicUrlFromTieBa(String url, boolean pageFlag) throws IOException {
		List<Element> elementList = Lists.newArrayList();
		int page = 1;// 起始页
		int shutdownCount = 0;// 终结次数
		int urlNumberOfOnePage = 0;// 某一页的图片数量
		try {
			while (true) {
				String urlWithPage = url;
				if (page != 1) {
					urlWithPage += "?pn=" + page;
				}
				logger.error("开始访问：{}", urlWithPage);
				Document document = Jsoup.parse(new URL(urlWithPage), 10 * 1000);
				Elements imgElementList = document.getElementsByClass("BDE_Image");
				// 我不敢合并list，只能先写个size小于10
				if (CollectionUtils.isEmpty(imgElementList) || imgElementList.size() < 8) {
					imgElementList = document.getElementsByClass("d_content_img");
				}

				if (CollectionUtils.isEmpty(imgElementList)) {
					logger.error("毛都没获取到");
					shutdownCount++;
				} else {
					// 塞值
					elementList.addAll(imgElementList);
					// 跳出计数
					if (urlNumberOfOnePage == imgElementList.size()) {
						logger.error("本次获取数量与上次一致");
						shutdownCount++;
					} else {
						urlNumberOfOnePage = imgElementList.size();
						logger.error("本次获取数量：{}", urlNumberOfOnePage);
					}
				}
				page++;
				// 这个9次，先将就着用吧
				if (shutdownCount > 6 || !pageFlag) {
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// 结果转换，搞不好会报空指针，不想管了
		List<String> urlList = Lists.transform(elementList, new Function<Element, String>() {
			public String apply(Element input) {
				return input.attr("src");
			}
		});

		// 去重
		logger.error("此时list长度为{}", urlList.size());
		LinkedHashSet<String> tempSet = Sets.newLinkedHashSet(urlList);
		urlList = Lists.newArrayList(tempSet);
		logger.error("去重后长度为{}", urlList.size());

		return urlList;
	}

	/**
	 * 从第一会所拿图片，这个破网站会通过sessionid来确认登录信息，<br>
	 * 所以只能把源代码保存至本地后，通过正则提取图片url（其实jsoup也可以，不过写正则方便点）<br>
	 * 
	 * @param regex
	 * @throws Exception
	 */
	public static void sis01(String regex) throws Exception {
		String content = FileUtil.getContentFromFile(PATH_OF_TITLE_AND_URL);
		List<String> urlList = RegexUtil.getMatchedString(content, regex);

		// 去重
		Set<String> tempSet = Sets.newLinkedHashSet(urlList);

		urlList.clear();
		urlList.addAll(tempSet);

		Map<String, List<String>> allPicUrlMap = Maps.newHashMap();
		allPicUrlMap.put("sis01", urlList);
		saveToLocal(allPicUrlMap);
	}

	/**
	 * 爬《我的英雄学院》，没有重用性，所以是private
	 * 
	 * @param url 万物起源，思路是这样的，尝试找“下一页”的a标签，没有就找“下一篇”<br>
	 *            下一页与无下一页
	 * @throws Exception
	 */
	private static void getMyHero(String url) throws Exception {
		WebDriver webDriver = SeleniumUtil.getWebDriver();
		webDriver.get(url);

	}

	private static void getMyHero(int i) throws Exception {
		String hua = i + "";
		if (i < 10) {
			hua = "0" + i;
		}

		String url = "https://www.myherocn.com/manhua/" + hua + ".shtml";
		WebDriver driver = SeleniumUtil.getWebDriver();
		driver.get(url);

		List<WebElement> elementList = driver.findElements(By.tagName(TAG_img));
		if (CollectionUtils.isEmpty(elementList)) {
			logger.error("没有获取到图片标签");
			return;
		}

		// 某一话的map
		Map<String, List<String>> huaMap = Maps.newHashMap();
		List<String> list = Lists.newArrayList();

		for (WebElement webElement : elementList) {
			String src = webElement.getAttribute(ATTR_src);
			logger.error("图片来源为：{}", src);
			if (StringUtils.startsWith(src, "http://www.myherocn.com/uploads/allimg")) {
				logger.error("加入");
				list.add(src);
			} else {
				logger.error("不加入");
			}
		}

		Document document = Jsoup.parse(driver.getPageSource());
		huaMap.put(document.title(), list);

		driver.close();
		saveToLocal(huaMap);
	}

	public static void main(String[] args) throws Exception {
		downMangaFromShuHui(11182);
		/*
		 * 猎人
		 */
//		downMangaFromShuHui(4978, 8979, 9002, 9027, 9040, 9061, 9088, 9130, 9166, 9233, 9284, 10430);
//		getMyHero();
	}
}
