package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class Fvloginpage {

	public WebDriver driver;
	
	public Fvloginpage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	

	WebElement common;
	String EmailAddress= "Email Address"; //TODO
	String Password= "Password"; //TODO
	public enum Fvloginpgelements{
		username,password,remembermecheck,loginbutton, facebookbutton, existLoginbutton;
	}
	public void LoginFV() throws InterruptedException
	{
		getElement(Fvloginpgelements.username).sendKeys(EmailAddress);
		System.out.println("FV UserName is: "+EmailAddress);
		Thread.sleep(2000);
		getElement(Fvloginpgelements.password).sendKeys(Password);
		Thread.sleep(2000);
		getElement(Fvloginpgelements.remembermecheck).click();
		Thread.sleep(2000);
		getElement(Fvloginpgelements.loginbutton).click();
		Thread.sleep(5000);
		CommonRepository.waitforPageLoad(driver);
	}
	public WebElement getElement(Fvloginpgelements webelement)
	{
		switch (webelement) 
		{
		case username:
			common=driver.findElement(By.xpath("//input[@class='form-control email']"));
			break;
		case password:
			common=driver.findElement(By.xpath("//input[@class='form-control password']"));
			break;
		case loginbutton:                                            
			//common=driver.findElement(By.xpath(".//*[@id='login']/div/div/div[1]/form/div[4]/button"));
			common=driver.findElement(By.xpath("(//button[@class='btn btn-primary btn-block submit'])[1]"));
			break;
		case existLoginbutton:                                            
			common=driver.findElement(By.xpath("//*[@id='login-options']/div/div/div[2]/div[4]/button"));
			break;
		case remembermecheck:
			//common=driver.findElement(By.cssSelector(".remember-me"));
			//common=driver.findElement(By.xpath("(//input[@class='remember-me'])[1]"));
			common=driver.findElement(By.xpath("(//input[@class='remember-me'])[1]"));
			break;
		case facebookbutton:
			common=driver.findElement(By.id("//span[text()='Facebook']"));
			break;
		default:
			break;
		}
		return common;
	}
}
