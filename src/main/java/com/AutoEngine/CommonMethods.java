package com.AutoEngine;
/**
 * Description: This is common utility file 
 * @author Rajat Meharwade
 *
 */
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
public class CommonMethods {
	public static Properties prop = null;
	public static WebDriver driver = null;
	public static final String CONFIG_FILE_PATH = "config.properties";
	public static final String JSON_FILE_PATH = "TestData.json";
	public static final Logger logger = LoggerFactory
			.getLogger(CommonMethods.class);
	public static final String WINDOWS_CHROME_DRIVER_PATH = "C:\\Users\\konatavi\\Documents\\BD Screenshots\\chromedriver.exe";
	public static final String WINDOWS_IE_DRIVER_PATH = "C:\\Users\\konatavi\\Documents\\BD Screenshots\\IEDriverServer.exe";
	public static final String LINUX_CHROME_DRIVER_PATH = "/usr/bin/google-chrome/chromedriver";
	public static final String MAC_CHROME_DRIVER_PATH = "/usr/bin/google-chrome/chromedriver";
	public static String OS = null;
	JavascriptExecutor js = (JavascriptExecutor) driver;
	public String number;
	public static ExtentReports extent;
	public static ExtentTest test;
	/**
	 * Description: Method to read data from 'config.properties' file
	 * 
	 * @author Rajat Meharwade
	 * @throws IOException
	 *
	 */
	public static String readPropertiesFile(String key) {
		prop = new Properties();
		try {
			prop.load(CommonMethods.class.getClassLoader()
					.getResourceAsStream(CONFIG_FILE_PATH));
		} catch (IOException e) {
			logger.debug("JSON file not found");
		}
		logger.debug("Returns the value of the Key " + key);
		return prop.getProperty(key);
	}
	/**
	 * Description: Method to initialize driver
	 */
	@SuppressWarnings("deprecation")
	public static void initDriver(String browser) {
		if (driver == null) {
			if (browser.equalsIgnoreCase("Firefox")) {
				driver = new FirefoxDriver();
				logger.debug("Firefox Driver Initilized...");
				// driver.manage().timeouts().implicitlyWait(60,
				// TimeUnit.SECONDS);
			} else if (browser.equalsIgnoreCase("IE")) {
				if (isWindows()) {
					logger.info(
							"Using the IE driver in windows machine at path "
									+ WINDOWS_IE_DRIVER_PATH);
					System.setProperty("webdriver.ie.driver",
							WINDOWS_IE_DRIVER_PATH);
				}
				driver = new InternetExplorerDriver();
				logger.debug("InternetExplorer Driver Initilized...");
				// driver.manage().timeouts().implicitlyWait(60,
				// TimeUnit.SECONDS);
			} else if (browser.equalsIgnoreCase("Chrome")) {
				if (isWindows()) {
					logger.info(
							"Using the Chrome driver in windows machine at path "
									+ WINDOWS_CHROME_DRIVER_PATH);
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
							true);
					System.setProperty("webdriver.chrome.driver",
							WINDOWS_CHROME_DRIVER_PATH);
				} else if (isMac()) {
					logger.info(
							"Using the Chrome driver in Mac machine at path "
									+ MAC_CHROME_DRIVER_PATH);
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
							true);
					System.setProperty("webdriver.chrome.driver",
							MAC_CHROME_DRIVER_PATH);
				} else {
					logger.info(
							"Using the Chrome driver in linux machine at path "
									+ LINUX_CHROME_DRIVER_PATH);
					System.setProperty("webdriver.chrome.driver",
							LINUX_CHROME_DRIVER_PATH);
				}
				driver = new ChromeDriver();
			} else {
				// default FF
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			}
		}
	}
	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}
	private static boolean isMac() {
		return (getOsName().indexOf("mac") >= 0);
	}
	public static void deleteCookiesIe() {
		try {
			Runtime.getRuntime().exec(
					"RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void captureScreenshot(String fileName) {
		try {
			new File("Reports/").mkdirs();
			logger.debug("Creates a directory 'Reports/'");
			FileOutputStream out = new FileOutputStream(
					"Reports/screenshot-" + fileName + ".png");
			out.write(((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.BYTES));
			logger.debug("Screenshot taken....");
			out.close();
		} catch (IOException e) {
			logger.error("Problem in taking screenshot");
		}
	}
	/**
	 * Description: Method to read test data from Json file
	 * 
	 * @author Rajat Meharwade
	 * @throws ParseException
	 *
	 */
	public String readDataFromJson(String testCaseName, String key) {
		String keyValue = null;
		JSONObject dataObj = null;
		JSONParser parser = new JSONParser();
		logger.debug("Created a Parser Object...");
		try {
			JSONObject fileObj = (JSONObject) parser.parse(
					new InputStreamReader(CommonMethods.class.getClassLoader()
							.getResourceAsStream(JSON_FILE_PATH)));
			logger.debug("Parsed the JSON File...");
			try {
				dataObj = (JSONObject) fileObj.get(testCaseName);
				logger.debug("Gets the testcases '" + testCaseName
						+ "' name from JSON Object");
			} catch (NullPointerException e) {
				logger.debug("TestCase Name '" + testCaseName
						+ "' did not match in JSON Object");
				System.exit(0);
			}
			try {
				String keyObj = (String) dataObj.get(key);
				logger.debug(
						"Gets the Value of the '" + key + "' from JSON Object");
				keyValue = keyObj.toString();
				logger.debug("Converts the Value '" + keyValue
						+ "' to Sting object");
			} catch (NullPointerException e) {
				logger.error("Key  '" + key + "' did not match in JSON Object");
				System.exit(0);
			}
		} catch (Exception ex) {
			logger.error("JSON File Not Found");
			ex.printStackTrace();
		}
		logger.debug("Returns '" + keyValue + "'");
		return keyValue;
	}
	/**
	 * Description: Method to check whether an object exists or not
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public boolean exists(By by) {
		try {
			logger.debug(
					"Verifies element is present or not, if element then returns 'true' or returns 'false' if element is not present");
			return driver.findElement(by) != null;
		} catch (NoSuchElementException e) {
			logger.error("Element not fount in 'exist() method");
			return false;
		}
	}
	/**
	 * Description: Method to set value in any edit box
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public static void setTextInEditBox(WebElement e, String text) {
		try {
			e.clear();
			e.click();
			e.sendKeys(text);
			wait(1000);
		} catch (NoSuchElementException ex) {
			logger.error(e + " Element not forund");
		}
	}
	/**
	 * Description: Method to get text from list of elements
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public static List<String> getTextFromElements(List<WebElement> elements) {
		List<String> values = new ArrayList<String>();
		try {
			for (WebElement e : elements) {
				values.add(e.getText());
			}
		} catch (NoSuchElementException ex) {
			logger.error("Element not forund in getTextFromElements() method");
		}
		return values;
	}
	/**
	 * Description: Method to get text from element
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public String getTextFromElement(WebElement e) {
		String textValue = null;
		try {
			textValue = e.getText();
			logger.debug("Get the text from the element " + e);
		} catch (NoSuchElementException ex) {
			logger.error("Element not forund in getTextFromElement() method");
		}
		return textValue;
	}
	/**
	 * Description: Method to set value in any check box
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public void setAnyCheckBox(WebElement e, String toSet) {
		try {
			if (toSet.toLowerCase().equals("on")
					|| toSet.toLowerCase().equals("Yes")
					|| toSet.toLowerCase().equals("yes")
					|| toSet.toLowerCase().equals("YES")) {
				if (!e.isSelected()) {
					e.click();
					logger.debug("Clicked on YES/ON [CheckBox]");
				}
			} else if (toSet.toLowerCase().equals("off")
					|| toSet.toLowerCase().equals("No")
					|| toSet.toLowerCase().equals("no")
					|| toSet.toLowerCase().equals("NO")) {
				if (e.isSelected()) {
					e.click();
					logger.debug("Clicked on NO/OFF [CheckBox]");
				}
			}
		} catch (NoSuchElementException ex) {
			logger.error("Element not forund in setAnyCheckBox() method");
		}
	}
	/**
	 * Description: Method to select any radio button
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public void selectAnyRadioButton(WebElement e, String toSet) {
		try {
			if (toSet.toLowerCase().equals("on")
					|| toSet.toLowerCase().equals("Yes")
					|| toSet.toLowerCase().equals("yes")
					|| toSet.toLowerCase().equals("YES")) {
				if (!e.isSelected()) {
					e.click();
					logger.debug("Clicked on YES/ON [RadioButton]");
				}
			} else if (toSet.toLowerCase().equals("off")
					|| toSet.toLowerCase().equals("No")
					|| toSet.toLowerCase().equals("NO")
					|| toSet.toLowerCase().equals("no")) {
				if (!e.isSelected()) {
					e.click();
					logger.debug("Clicked on NO/OFF [RadioButton]");
				}
			}
		} catch (NoSuchElementException ex) {
			logger.error("Element not forund in selectAnyRadioButton() method");
		}
	}
	/**
	 * Description: Method to click any object
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public static void clickAnyObject(WebElement e) {
		try {
			e.click();
			logger.debug("Clicked the WebElement " + e);
		} catch (NoSuchElementException ex) {
			logger.error("WebElement " + e + " Not Present");
		}
	}
	/**
	 * Description: Method to find an element by text
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public WebElement findElementByText(List<WebElement> elements,
			String text) {
		try {
			for (WebElement e : elements) {
				if (text.equals(e.getText())) {
					return e;
				}
			}
		} catch (NoSuchElementException ex) {
			logger.error("WebElement Not Present in findElementByText()");
		}
		return null;
	}
	/**
	 * Description: Method to select a value from weblist
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	protected void selectOptionFromList(WebElement e, String option) {
		Select select = new Select(e);
		logger.debug("Initialized the Select class...");
		try {
			select.selectByVisibleText(option);
			logger.debug("Selected " + option + " from Dropdown.");
			wait(1000);
		} catch (NoSuchElementException ex) {
			logger.error("Value " + option + " not found in Dropdown");
		}
	}
	/**
	 * Description: Method to find list of elements
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public List<WebElement> findElements(By by) {
		List<WebElement> elements = new ArrayList<WebElement>();
		try {
			elements = driver.findElements(by);
		} catch (NoSuchElementException e) {
			logger.error("Element " + elements + " not found");
		}
		return elements;
	}
	/**
	 * Description: Method to find an element by reference
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public WebElement findElement(By by, int index) {
		List<WebElement> elements = findElements(by);
		try {
			if (elements.size() > 0 && index < elements.size()) {
				return elements.get(index);
			}
		} catch (NoSuchElementException e) {
			logger.error("Element not present");
		}
		return null;
	}
	/**
	 * Description: Method to wait till any object exists
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public WebElement waitForAnyObject(WebElement element) {
		@SuppressWarnings("deprecation")
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement Element = wait
				.until(ExpectedConditions.visibilityOf(element));
		return Element;
	}
	public List<WebElement> waitForListOffObjects(By by) {
		@SuppressWarnings("deprecation")
		WebDriverWait wait = new WebDriverWait(driver, 180);
		List<WebElement> Elements = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
		return Elements;
	}
	public static WebElement waitForElement(final By locator,
			int timeoutSeconds) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(180))
				.pollingEvery(Duration.ofMillis(300))
				.ignoring(NoSuchElementException.class);
		return wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver webDriver) {
				return driver.findElement(locator);
			}
		});
	}
	public static WebElement fluentWait(final By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(180))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(org.openqa.selenium.NoSuchElementException.class);
		WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		});
		return foo;
	};
	public static boolean waitForElement(WebDriver driver, int timeout) {
		boolean check = false;
		int count = 0;
		while (!check) {
			try {
				Set<String> winHandle = driver.getWindowHandles();
				if (winHandle.size() > 1) {
					check = true;
					return check;
				}
				Thread.sleep(1000);
				count++;
				if (count > timeout) {
					return check;
				}
			} catch (Exception e) {
			}
		}
		return check;
	}
	/**
	 * Description: Method to exit driver
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public static void quitDriver() {
		if (driver != null) {
			logger.debug("Checks if driver is not null");
			driver.quit();
			logger.debug("Driver got quit");
			driver = null;
			logger.debug("Initializing the driver to null");
		}
	}
	/**
	 * Description: Method for generic wait
	 * 
	 * @author Rajat Meharwade
	 *
	 */
	public static void wait(int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
		}
	}
	public WebElement findElement(By by) {
		return findElement(by, 0);
	}
	public static void setTextInEditBoxwithWait(WebElement e, String text) {
		e.clear();
		logger.debug("Clears the input field");
		e.click();
		e.sendKeys(text);
		logger.debug("Enters the text " + text + " in the input field");
		wait(Integer.parseInt(
				readPropertiesFile("background.direct.page.load.time")));
		e.sendKeys(Keys.ARROW_LEFT);
	}
	public static void clickAndSaveFileIE(WebElement element)
			throws InterruptedException {
		try {
			Robot robot = new Robot();
			// get the focus on the element..don't use click since it stalls the
			// driver
			element.sendKeys("");
			// simulate pressing enter
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			// wait for the modal dialog to open
			Thread.sleep(2000);
			// press s key to save
			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_S);
			Thread.sleep(2000);
			// press enter to save the file with default name and in default
			// location
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	public static void logout() {
		driver.findElement(By.xpath("//a[@class='top_logout_button']")).click();
		logger.debug("Click logout...");
		wait(5000);
	}
	public String randomString() {
		return RandomStringUtils.random(10, 'a', 'b', 'e', 'g', 'h', 'i', 'j',
				'k', 'l', 'm');
	}
	public String randomLastName() {
		return RandomStringUtils.random(5, 'a', 'b', 'e', 'g', 'h');
	}
	// This method will generate random password with initial character in upper
	// case and the remaining characters in
	// lower case and any one digit within 1-10
	public String randomWord() {
		String str = RandomStringUtils.random(8, 'a', 'b', 'e', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z');
		String s = Character.toUpperCase(str.charAt(0)) + str.substring(1);
		Random rand = new Random();
		int n = rand.nextInt(10);
		String pass = s + n;
		System.out.println("Password Length -->" + pass.length());
		return pass;
	}
	public int getRandomNumber() {
		Random t = new Random();
		return t.nextInt(10000);
	}
	public String getRandomName() {
		Random t = new Random();
		int number = t.nextInt(10000);
		String str = RandomStringUtils.random(5, 'a', 'b', 'e', 'g', 'h');
		String name = Integer.toString(number) + str;
		return name;
	}
	public void getListView(By by) {
		WebElement element = driver.findElement(by);
		js.executeScript("arguments[0].click();", element);
	}
	public void getGridView(By by) {
		WebElement element = driver.findElement(by);
		js.executeScript("arguments[0].click();", element);
	}
}
