package com.AutoEngine;
import com.relevantcodes.extentreports.ExtentReports;


public class ExtentClass {
	
	 public static ExtentReports extent;
	    
	    @SuppressWarnings("deprecation")
		public static ExtentReports getInstance() {
	        if (extent == null) {
	            extent = new ExtentReports("D://BackgroundDirect.html", true);
	            
	            // optional
	            extent.config()
	                .documentTitle("Automation Report")
	                .reportName("Regression")
	                .reportHeadline("Test Suite Report");
	               
	            // optional
	            extent
	                .addSystemInfo("Selenium Version", "2.47")
	                .addSystemInfo("Environment", "BANGALORE QC");
	        }
	        return extent;
	    }
	

}
