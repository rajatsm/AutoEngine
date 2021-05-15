package com.AutoEngine;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReport{
	
	
	//static File filePath = new File("D:\\Report\\Background.html");
	static ExtentReports extent = new ExtentReports("D:\\Report\\Background.html", true);
	static ExtentTest test;
	
	public static void flushReport(){
		extent.flush();
	}
	public static void startTest(){
		 test = extent.startTest("BD", "Background Direct Automation");
	}
	
	public static void endTest(){
		extent.endTest(test);
	}
	
	public static void logPass(String pass){
		test.log(LogStatus.PASS,pass);
	}
	
	public static void logInfo(String info){
		test.log(LogStatus.INFO,info);
	}
	
	public static void logError(String err){
		test.log(LogStatus.ERROR,err);
	}
}
