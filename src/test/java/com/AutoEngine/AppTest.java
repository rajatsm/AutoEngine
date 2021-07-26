package com.AutoEngine;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	private WebDriver driver;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
    }

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.edgedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeMethod
	public void setupTest() {
        driver = new ChromeDriver();
    }
    /**
     * Rigourous Test :-)
     * @throws InterruptedException 
     */
    @Test
	public void testApp() throws InterruptedException
    {
    	//System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") +"/drivers/chromedriver");
        //WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");
        Thread.sleep(5000);
        driver.quit();
    }
}
