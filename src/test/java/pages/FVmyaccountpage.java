package pages;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class FVmyaccountpage{

	public WebDriver driver;
	
	public FVmyaccountpage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	
	WebElement common;
	String fbemail= "FVEmail"; //TODO
	
	public enum FVmyaccntpgelements {

		myprofile,myaccnt, fvlogout, unlink, unlinkaccount, getstarted, Email, fvprofileEmail, getStartedtolinkUV;
	}
	public void verifyUnlinkUV()
	{
		WebElement getstarted=getElement(FVmyaccntpgelements.getstarted);
		if(getstarted.isDisplayed())
		{	
			assertFalse("Verify UnlinkUV - UV account is unlinked successfully", false);
		}
		else
		{
			assertFalse("Verify UnlinkUV - UV Unlinking", true);	
		}
	}
	public void verifyFVlogin()
	{
		System.out.println("I am from the Collection FV login method");
		if(getElement(FVmyaccntpgelements.myprofile).isDisplayed())
		{
			assertFalse("Verify Login - Login Validation", false);	
		}
		else
		{
			assertFalse("Verify Login - Login Validation", true);	
		}
	}
	public void verifyAccountCreation()
	{
		if(getElement(FVmyaccntpgelements.myprofile).isDisplayed())
		{
			assertFalse("Verify FVAccountCreation - Validate the FVAccount Creation ", false);
		}
		else
		{
			assertFalse("Verify FVAccountCreation -  Validate the FVAccount Creation ", true);
		}
	}
	public void verifyProfileEmail(String regEmail,String accntEmail)
	{
		if(regEmail.equalsIgnoreCase(accntEmail))
		{
			assertFalse("Verify RegisteredEmail - Validate Email", false);	
		}
		else
		{
			assertFalse("Verify RegisteredEmail - Validate Email", true);	
		}
	}
	public WebElement getElement(FVmyaccntpgelements webelement)
	{
		switch (webelement) 
		{
		case myprofile:
			//common=driver.findElement(By.cssSelector(".dropdown-toggle.profile"));
			common=driver.findElement(By.xpath(".//*[@id='fb-root']/header/div/ul[2]/li[1]/a/span[1]/img"));
			break;
		case myaccnt:
			common=driver.findElement(By.linkText("My Account"));
			break;
		case fvlogout:
			common=driver.findElement(By.linkText("Log Out"));
			break;
		case unlink:
			common=driver.findElement(By.linkText("Unlink"));
			break;
		case unlinkaccount:
			common=driver.findElement(By.xpath("//button[@class='btn btn-delete btn-lg submit']"));
			break;
		case getstarted:
			common=driver.findElement(By.linkText("Get Started"));
			break;
		case Email:                             
			common=driver.findElement(By.xpath(".//*[@id='profile']/div[7]/div/div[2]/div/div/div[1]"));
			break;
		case fvprofileEmail:
			common=driver.findElement(By.xpath(".//*[@id='profile']/div[7]/div/div/div/div[2]/span"));
			break;
		case getStartedtolinkUV:
			common=driver.findElement(By.xpath(".//*[@id='profile']/div[7]/div/div[2]/div/div/div[2]/a"));
			break;
		default:
			break;
		}
		return common;
	}
}
