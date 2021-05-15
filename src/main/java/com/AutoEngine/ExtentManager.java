package com.AutoEngine;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {

	@SuppressWarnings("deprecation")
	public static ExtentReports Instance(String suite) {
		ExtentReports extent;
		String Path = "D://Reports/" + suite + ".html";
		System.out.println(Path);
		extent = new ExtentReports(Path, false);
		extent.config().documentTitle("Background Direct Automation Report").reportName("BD Regression Test Suite");

		return extent;
	}

	public static String CaptureScreen(WebDriver driver, String ImagesPath) {
		TakesScreenshot oScn = (TakesScreenshot) driver;
		File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		File oDest = new File(ImagesPath + ".jpg");
		try {
			FileUtils.copyFile(oScnShot, oDest);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ImagesPath + ".jpg";
	}
}