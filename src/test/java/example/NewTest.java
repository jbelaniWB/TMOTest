package example;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.net.MalformedURLException;

import org.openqa.selenium.By;		
import org.openqa.selenium.WebDriver;		
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class NewTest {
	private WebDriver driver;		
	@Test				
	public void testEasy() {	
		driver.get("http://demo.guru99.com/selenium/guru99home/");  
		String title = driver.getTitle();				 
		Assert.assertTrue(title.contains("Demo Guru99 Page")); 		
	}	
	@BeforeTest
	public void beforeTest() {	
		System.setProperty("webdriver.chrome.driver", "/Users/qamacbookpro/documents/SupportLibraries/chromedriver");
	    driver = new ChromeDriver();
//		try {
//			driver = new RemoteWebDriver(new java.net.URL("http://127.0.0.1:7055"), DesiredCapabilities.chrome());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}		
	@AfterTest
	public void afterTest() {
		driver.quit();			
	}
}
