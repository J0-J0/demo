package com.jojo.util;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumUtil {

	private static final String CHROME_DRIVER = "webdriver.chrome.driver";

	private static final String FIREFOX_DRIVER = "webdriver.firefox.marionette";
	private static final String FIREFOX_BIN = "webdriver.firefox.bin";
	// 这个东西会动态变的
	private static final String FIREFOX_BIN_PATH = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

	static {
		String projectDirectory = System.getProperty("user.dir");
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

	/**
	 * 
	 * @param browser
	 * @return
	 */
	public static WebDriver getFirefoxWebDriver() {
		return new FirefoxDriver();
	}

	public static void main(String[] args) {
	}
}
