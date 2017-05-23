package pages;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class Fvhomepage {

	/**
	 * @author 416467
	 * @param scripthelper
	 */
	public WebDriver driver;
	
	public Fvhomepage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	
	public WebElement common,fvlogo;
	String exptitle= "Redeem your UltraViolet Digital Copy - Flixster Video";
	String actualtitle;
	String strURL = "https://www.flixstervideo.com";
	public static String url = "https://www.flixstervideo.com";
	
	public static Fvhomepage visitPage(WebDriver driver) {
		Fvhomepage page = new Fvhomepage(driver);
        page.visitPage();
        return page;
    }
	
	public enum Fvhomepgelements {	
		searchresult,enterredeemcode,redeemcontinue,filterdrpdown,sortbydrpdown,termsofservice,privacypolicy,
		coprights,countryseldropdown,fvlogo,redeem,mycollection,signup,fvlogin,help,searchtextbox,searchbtn, 
		clickhere, errconsumedtoken, errorinvalidtoken,erroranotherregiontoken;
	}
	
	public void visitPage() {
        this.driver.get(url);
    }
	
	public void verifyHomepage() throws Exception
	{
		fvlogo=getElement(Fvhomepgelements.fvlogo);
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		
		if(fvlogo.isDisplayed())
		{	
			actualtitle=driver.getTitle();
			if(actualtitle.equalsIgnoreCase(exptitle))
			{
				assertFalse("Verify Homepage - Validate Application Home page title", false);
			}
			else
			{
				assertFalse("Verify Homepage - Validate Application Home page title", true);
			}
		}
		else
		{
			assertFalse("Verify Homepage - Validate Application Home page title", true);	
		}
	}
	
	public WebElement getElement(Fvhomepgelements webelement)
	{
		switch (webelement) 
		{
		case fvlogo:
			common=driver.findElement(By.xpath(".//*[@id='fb-root']/header/div/ul[1]/li[2]/a"));
			//common=driver.findElement(By.xpath("html/body/header/div/ul[1]/li[3]/a"));
			break;
		case redeem:
			common=driver.findElement(By.linkText("Redeem"));
			break;
		case mycollection:
			common=driver.findElement(By.xpath(".//*[@id='fb-root']/header/div/ul[1]/li[4]/a"));
			break;
		case clickhere:
			common=driver.findElement(By.xpath("//*[@id='login-options']/div/h4[2]/a"));  
			break;
		case signup:
			common=driver.findElement(By.linkText("Sign Up"));
			break;
		case fvlogin:
			common=driver.findElement(By.linkText("Log In"));
			break;
		case help:
			common=driver.findElement(By.linkText("Help"));
			break;
		case searchbtn:
			common=driver.findElement(By.xpath("//button[@type='submit']"));
			break;
		case searchtextbox:
			common=driver.findElement(By.xpath("//input[@type='search']"));
			break;
		case filterdrpdown:
			common=driver.findElement(By.xpath("//*[@id='title-filters']/div/div[1]/div/button/span[1]"));
			break;
		case sortbydrpdown:
			common=driver.findElement(By.xpath("//*[@id='title-filters']/div/div[2]/div/button"));
			break;
		case coprights:
			common=driver.findElement(By.xpath("html/body/footer/div[2]/div[3]/span[1]"));
			break;
		case termsofservice:
			common=driver.findElement(By.xpath("html/body/footer/div[2]/div[3]/span[2]/a[1]"));
			break;
		case privacypolicy:
			common=driver.findElement(By.xpath("html/body/footer/div[2]/div[3]/span[2]/a[2]"));
			break;
		case searchresult:
			common=driver.findElement(By.xpath("//*[@id='title-results']/div[1]/a/img"));
			break;
		case redeemcontinue:
			common=driver.findElement(By.xpath("//button[@type='submit']"));
			break;
		case enterredeemcode: 
			common=driver.findElement(By.xpath("//input[@type='text']"));
			break;
		case countryseldropdown:
			common=driver.findElement(By.id("debug-country"));
			break;
		case errconsumedtoken:
			common=driver.findElement(By.cssSelector(".alert.alert-danger.error-service"));
			break;
		case errorinvalidtoken:
			common=driver.findElement(By.cssSelector(".alert.alert-danger.error-service"));
			break;	
		case erroranotherregiontoken:
			common=driver.findElement(By.cssSelector(".alert.alert-danger.error-service"));
			break;	
		default:
			break;
		}
		return common;
	}
}
