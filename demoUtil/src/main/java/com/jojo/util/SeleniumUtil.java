package com.jojo.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;

public class SeleniumUtil {

    private static final String CHROME_DRIVER = "webdriver.chrome.driver";

    private static final String FIREFOX_DRIVER = "webdriver.firefox.marionette";
    private static final String FIREFOX_BIN = "webdriver.firefox.bin";
    // 这个东西会动态变的
    private static final String FIREFOX_BIN_PATH = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

    static {
        String projectDirectory = System.getProperty("user.dir") + File.separator + "demoUtil";
        String driverDirectory = projectDirectory + File.separator + "browserDriver" + File.separator;

        // Google
        String chromeDriverPath = driverDirectory + "chromedriver.exe";
        System.getProperties().setProperty(CHROME_DRIVER, chromeDriverPath);

        // firefox
        String firefoxDriverPath = driverDirectory + "geckodriver.exe";
        System.getProperties().setProperty(FIREFOX_DRIVER, firefoxDriverPath);
        System.getProperties().setProperty(FIREFOX_BIN, FIREFOX_BIN_PATH);
    }

    /**
     * 默认使用chrome
     *
     * @return
     */
    public static WebDriver getWebDriver() {
        return new ChromeDriver();
    }

    public static WebDriver getWebDriver(String driverName) {
        if (StringUtils.equals(driverName, CHROME_DRIVER)) {
            return new ChromeDriver();
        } else if (StringUtils.equals(driverName, FIREFOX_DRIVER)) {
            return new FirefoxDriver();
        }
        return null;
    }


    public static void main(String[] args) {
        WebDriver webDriver = getWebDriver();
        webDriver.get("https://www.webmota.com/comic/chapter/zhabixiaoxin-jiujingyiren/0_0.html");
        System.out.println(webDriver.getPageSource());
    }
}
