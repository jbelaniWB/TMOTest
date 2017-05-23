package pages;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


public class Facebookpage{
	public WebDriver driver;
	public Facebookpage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

	public WebElement common;
	String FBEmailAddress= "FBEmail"; //TODO
	String FBPassword= "FBPassword"; //TODO
	public enum facebookpgelements{ 
		fbusername,fbpassword,fblogin,fbokaybutton, Month, Day, Year;
	}
	public  void fbLogin() throws InterruptedException
	{
		String windowtitle = "Facebook";
		String currentWindow = driver.getWindowHandle();
		Set<String> availableWindows = driver.getWindowHandles();
		if (!availableWindows.isEmpty()) {
			for (String windowId : availableWindows) {
				String switchedWindowTitle=driver.switchTo().window(windowId).getTitle();
				if ((switchedWindowTitle.equals(windowtitle))||(switchedWindowTitle.contains(windowtitle)))
				{
					driver.manage().window().maximize();
					getElement(facebookpgelements.fbusername).sendKeys(FBEmailAddress);
					getElement(facebookpgelements.fbpassword).sendKeys(FBPassword);
					getElement(facebookpgelements.fblogin).click();
					System.out.println("Entered Credentials");
					Thread.sleep(1000);
					driver.switchTo().window(currentWindow);
					break;
				} else {
					driver.switchTo().window(currentWindow);
				}
			}		
			//driver.switchTo().window(currentWindow);
		}
		System.out.println("Switched to Current Window");
		driver.switchTo().window(currentWindow);
	}
	public WebElement getElement(facebookpgelements test)
	{
		switch (test) 
		{
		case fbusername:
			common=driver.findElement(By.id("email"));
			break;
		case fbpassword:
			common=driver.findElement(By.id("pass"));
			break;
		case fblogin:
			common=driver.findElement(By.xpath("//input[@name='login']"));
			break;
		case fbokaybutton:
			common=driver.findElement(By.xpath("//button[@name='__CONFIRM__']"));
			break;
		case Month:
			common=driver.findElement(By.xpath("//select[@id='user_birthday_2i']"));
			break;
		case Day:
			common=driver.findElement(By.xpath("//select[@id='user_birthday_3i']"));
			break;
		case Year:
			common=driver.findElement(By.xpath("//select[@id='user_birthday_1i']"));
			break;
		default:
			break;
		}
		return common;
	}
}
