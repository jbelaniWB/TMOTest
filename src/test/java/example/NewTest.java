package example;

import org.testng.annotations.Test;

import Pages.GuineaPigPage;

import org.testng.Assert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;		
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class NewTest extends TestBase{
//	private WebDriver driver;		
//	@Test				
//	public void testEasy() {	
//		driver.get("http://demo.guru99.com/selenium/guru99home/");  
//		String title = driver.getTitle();				 
//		Assert.assertTrue(title.contains("Demo Guru99 Page")); 		
//	}	
//	@BeforeTest
//	public void beforeTest() {	
//		System.setProperty("webdriver.chrome.driver", "chromedriver");
//	    driver = new ChromeDriver();
////		try {
////			driver = new RemoteWebDriver(new java.net.URL("http://127.0.0.1:7055"), DesiredCapabilities.chrome());
////		} catch (MalformedURLException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}		
//	@AfterTest
//	public void afterTest() {
//		driver.quit();			
//	}
	@org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
    public void verifyCommentInputTest(String browser, String version, String os, Method method)
            throws MalformedURLException, InvalidElementStateException, UnexpectedException {
        this.createDriver(browser, version, os, method.getName());
        WebDriver driver = this.getWebDriver();

        String commentInputText = UUID.randomUUID().toString();

        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        page.submitComment(commentInputText);

        Assert.assertTrue(page.getSubmittedCommentText().contains(commentInputText));
    }
}
