package pages;

import static org.junit.Assert.assertFalse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class FVsignuppage {
	public WebDriver driver;

	public FVsignuppage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.visitPage();
	}

	public WebElement common;
	String EmailAddress = "",
			Password = "pass12",
			ConfirmPassword = "pass12",
			FirstName = "FirstSauce",
			LastName = "LastSauce",
			Month = "January",
			Day = "1", Year = "1982",
			Country = "United States";
	String url = "http://www.flixstervideo.com";

	public void visitPage() {
        this.driver.get(url);
    }
	
	public enum Fvsignuppgelements {
		clickhere, EmailAddress, Password, ConfirmPassword, FirstName, LastName, Month, Day, Year, flixsterterms, SameUVoption, existuv, uvnotatthistime, continuebutton, facebookbutton, detectedUVEmail;
	}

	public void dateofBirth() throws Exception {
		driver.findElement(By.xpath("//select[@name='user[birthday(2i)]']")).sendKeys(Month);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//select[@name='user[birthday(3i)]']")).sendKeys(Day);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//select[@name='user[birthday(1i)]']")).sendKeys(Year);
	}

	public void FVSignupForm() throws InterruptedException {
		getElement(FVsignuppage.Fvsignuppgelements.Password).sendKeys(Password);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.ConfirmPassword).sendKeys(ConfirmPassword);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.FirstName).sendKeys(FirstName);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.LastName).sendKeys(LastName);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.Month).sendKeys(Month);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.Day).sendKeys(Day);
		Thread.sleep(3000);
		getElement(FVsignuppage.Fvsignuppgelements.Year).sendKeys(Year);
		Thread.sleep(1000);
		getElement(FVsignuppage.Fvsignuppgelements.flixsterterms).click();
		Thread.sleep(1000);
	}

	public void detectexistUV(String regEmail, String detectedMail) throws InterruptedException {
		Thread.sleep(1000);
		if (regEmail.contains(detectedMail)) {
			assertFalse("Verify the UVemail detection - Detect Existing UVmail", false);
		} else {
			assertFalse("Verify the UVemail detection - Detect Existing UVmail", true);
		}
	}

	public WebElement getElement(Fvsignuppgelements webelement) {
		switch (webelement) {
		case clickhere:
			common = driver.findElement(By.linkText("Click here"));
			break;
		case EmailAddress:
			common = driver.findElement(By.id("input-email"));
			break;
		case Password:
			common = driver.findElement(By.id("input-password"));
			break;
		case ConfirmPassword:
			common = driver.findElement(By.id("input-confirm-password"));
			break;
		case FirstName:
			common = driver.findElement(By.id("input-first-name"));
			break;
		case LastName:
			common = driver.findElement(By.id("input-last-name"));
			break;
		case Month:
			common = driver.findElement(By.id("user_birthday_2i"));
			break;
		case Day:
			common = driver.findElement(By.id("user_birthday_3i"));
			break;
		case Year:
			common = driver.findElement(By.id("user_birthday_1i"));
			break;
		case flixsterterms:
			common = driver.findElement(By.xpath("//input[@name='flixster_terms']"));
			break;
		case detectedUVEmail:
			common = driver.findElement(By.cssSelector(".user-email"));
			break;
		case SameUVoption:
			common = driver.findElement(By.xpath("//input[@value='create_same']"));
			break;
		case existuv:
			common = driver.findElement(By.xpath("//input[@value='login']"));
			break;
		case continuebutton:
			common = driver.findElement(By.cssSelector(".btn.btn-primary.btn-lg.submit"));
			break;
		case uvnotatthistime:
			common = driver.findElement(
					By.xpath("//*[@id='register']/div/div[1]/form/div[1]/div[3]/div/div/div[3]/div[1]/div[2]/label"));
			// common=driver.findElement(By.xpath(".//*[@id='register']/div/div[1]/form/div[1]/div[3]/div/div/div[3]/div[2]/div[4]/label"));
			break;
		case facebookbutton:
			common = driver.findElement(By.xpath("//span[text()='Facebook']"));
			break;
		default:
			break;
		}
		return common;
	}
}