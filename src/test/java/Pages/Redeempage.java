package pages;

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class Redeempage {
	public WebDriver driver;

	public Redeempage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public enum Redeempgelements {
		signup, gotomycollection, optincheck, completeredemption, uvpassword, uvconfirmpassword, continued, alreadyhaveUV, redeem, pgtextaftrredeem, createnewuv, Month, Day, Year, pgtextaftrreg, scroll2top, agreeTermsConditions;
	}

	WebElement common;
	String Titlename = "";
	String smartTitle = "";

	public void validateTitleRedemption() throws InterruptedException {
		Thread.sleep(3000);
		String test = getElement(Redeempgelements.pgtextaftrreg).getText();
		if (test.contains(Titlename)) {
			assertFalse("Title Redemption - Redeem successful @" + driver.getCurrentUrl(), false);
		} else {
			assertFalse("Title Redemption - Redeem successful @" + driver.getCurrentUrl(), false);
		}
	}

	public void validateRegistratrionredemption() {
		common = getElement(Redeempgelements.gotomycollection);
		if (common.isDisplayed()) {
			assertFalse("New FV New UV Registration-Redemption - Registration-Redeem successful", false);
		} else {
			assertFalse("New FV New UV Registration-Redemption - Registration-Redeem successful", true);
		}
	}

	public void checkOptin() {
		getElement(Redeempgelements.optincheck).click();// uncheck
		getElement(Redeempgelements.optincheck).click();// check
	}

	public void verifyOptin() {
		if (getElement(Redeempgelements.optincheck).isDisplayed())
			assertFalse("Verify Optincheck - Optin Verification", false);
		else
			assertFalse("Verify Optincheck - Optin Verification", true);
	}

	public void verifyTokenentrypage() throws InterruptedException {
		Thread.sleep(3000);
		String test = getElement(Redeempgelements.pgtextaftrredeem).getText();
		if (test.contains(Titlename)) {
			assertFalse("Verify Navigation to Code Entrypage - Title search and Navigate to Code Entry page ", false);
		} else {
			assertFalse("Verify Code Redemption - Invalid RedemptionCode", true);
		}
	}

	public void validRedeemcode() throws InterruptedException {
		Thread.sleep(3000);
		String test = getElement(Redeempgelements.pgtextaftrredeem).getText();
		System.out.println("The redeemed Title: " + test);
		if (test.contains(Titlename)) {
			assertFalse("Verify Redemption code - Valid Redeemptioncode  ", false);
		} else {
			System.out.println("Title Redemption Failed: " + test);
			assertFalse("Verify Code Redemption - Invalid RedemptionCode", true);
		}
	}

	public void verifysmartTitle() throws InterruptedException {
		Thread.sleep(3000);
		String test = getElement(Redeempgelements.pgtextaftrredeem).getText();
		if (test.contains(smartTitle)) {
			assertFalse("Verify-titlecode-smartpage - Valid-Redeemptioncode-Smartpage ", false);
		} else {
			assertFalse("Verify-titlecode-smartpage - InValid-Redeemptioncode-Smartpage ", true);
		}
	}

	public WebElement getElement(Redeempgelements webelement) {
		switch (webelement) {
		case signup:
			common = driver.findElement(By.xpath(".//*[@id='fb-root']/header/div/ul[2]/li[1]/a"));
			break;
		case redeem:
			common = driver.findElement(By.xpath(".//*[@id='fb-root']/header/div/ul[1]/li[3]/a"));
			break;
		case gotomycollection:
			common = driver.findElement(By.xpath("//div[@id='redeem']/div[3]/div[2]/div[1]/a"));
			break;
		case optincheck:
			common = driver.findElement(By.xpath("//input[@name='marketing_opt_in']"));
			break;
		case completeredemption:
			common = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-lg submit']"));
			break;
		case uvpassword:
			common = driver.findElement(By.xpath("//input[@name='uv_link_password']"));
			break;
		case uvconfirmpassword:
			common = driver.findElement(By.xpath("//input[@name='uv_link_confirm_password']"));
			break;
		case continued:
			common = driver.findElement(By.xpath("//button[@class='btn btn-primary btn-lg submit']"));
			break;
		case alreadyhaveUV:
			common = driver.findElement(By.xpath("//input[@value='login']"));
			break;
		case createnewuv:
			common = driver.findElement(By.xpath("//input[@value='create']"));
			break;
		case pgtextaftrredeem:
			common = driver.findElement(By.xpath("//*[@id='redeem']/div[2]/div[2]/h3/span"));
			break;
		case pgtextaftrreg:
			common = driver.findElement(By.xpath("//*[@id='redeem']/div[2]/div[2]/h3/span"));
			break;
		case Month:
			common = driver.findElement(By.xpath("//select[@id='user_birthday_2i']"));
			break;
		case Day:
			common = driver.findElement(By.xpath("//select[@id='user_birthday_3i']"));
			break;
		case Year:
			common = driver.findElement(By.xpath("//select[@id='user_birthday_1i']"));
			break;
		case scroll2top:
			common = driver.findElement(By.cssSelector(".fa.fa-chevron-up"));
			break;
		case agreeTermsConditions:
			common = driver.findElement(By.cssSelector(".flixster-terms-label>input"));
			break;
		default:
			break;
		}
		return common;
	}

	// API & External calls
	public void resetToken(String code) throws Exception {
		// "c1jq1Z25mMAJW4Jj2Gk9f3HsjyLapSFXMYfAdW2eRkjRsraYcCXGPbAsPdaME7fNcm5sxEY7eVnFgYzMjLq7VWjt7CBRxAKbwvdqWZtSP1gzT4hDdbQvdYFuqSPQ8YlK"
		// "DELUXE_CRS_CLP"
		String urlParameters = "";
		String url = "https://tms3.warnerbros.com/tms/rest/Token/reset/token_value/" + code;

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Encoding", "gzip,deflate");
		con.setRequestProperty("Content-Type", "application/xml");
		con.setRequestProperty("Accept", "application/xml");
		con.setRequestProperty("User-Agent", "Apache-HttpClient/4.5.1");
		con.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
		con.setRequestProperty("Client-ID", "DELUXE_CRS_CLP");
		con.setRequestProperty("Client-Key",
				"c1jq1Z25mMAJW4Jj2Gk9f3HsjyLapSFXMYfAdW2eRkjRsraYcCXGPbAsPdaME7fNcm5sxEY7eVnFgYzMjLq7VWjt7CBRxAKbwvdqWZtSP1gzT4hDdbQvdYFuqSPQ8YlK");
		// Send post request
		con.setDoOutput(true);
		con.connect();
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
	}
}
