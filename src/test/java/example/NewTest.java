package example;

import org.testng.annotations.Test;

import pages.CommonRepository;
import pages.FVsignuppage;
import pages.Fvhomepage;
import pages.GuineaPigPage;
import pages.Redeempage;
import pages.FVsignuppage.Fvsignuppgelements;

import org.testng.Assert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.UnexpectedException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class NewTest extends TestBase{
	FVsignuppage fvsignuppage;
	Redeempage redeempage;
	CommonRepository commonRepository;
	Fvhomepage fvhomepage;
	String titleName = "Suicide Squad";
	String token = "9CY74XFCXJQX";
	
//	@org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
//    public void verifyCommentInputTest(String browser, String version, String os, Method method)
//            throws MalformedURLException, InvalidElementStateException, UnexpectedException {
//        this.createDriver(browser, version, os, method.getName());
//        WebDriver driver = this.getWebDriver();
//
//        String commentInputText = UUID.randomUUID().toString();
//
//        GuineaPigPage page = GuineaPigPage.visitPage(driver);
//
//        page.submitComment(commentInputText);
//
//        Assert.assertTrue(page.getSubmittedCommentText().contains(commentInputText));
//    }
	
	@org.testng.annotations.Test(dataProvider = "hardCodedBrowsers")
	public void RedeemTokenAndRegisterNewAccount(String browser, String version, String os, Method method)
            throws Exception {
		this.createDriver(browser, version, os, method.getName());
        WebDriver driver = this.getWebDriver();
        fvsignuppage = new FVsignuppage(driver);
        redeempage = new Redeempage(driver);
        commonRepository = new CommonRepository();
        fvhomepage = new Fvhomepage(driver);
        
        // Reset Redemption Token
        //redeempage.resetToken(token);
        //fvhomepage.getElement(Fvhomepage.Fvhomepgelements.region).click();
        Thread.sleep(2000);
        fvhomepage.getElement(Fvhomepage.Fvhomepgelements.redeem).click();
		CommonRepository.waitforPageLoad(driver);
		fvhomepage.getElement(Fvhomepage.Fvhomepgelements.searchtextbox).sendKeys(titleName);
		Thread.sleep(4000);
		WebElement common=fvhomepage.getElement(Fvhomepage.Fvhomepgelements.searchbtn);
		CommonRepository.clickElementJscript(driver, common);
		
		CommonRepository.waitforPageLoad(driver);
		common=fvhomepage.getElement(Fvhomepage.Fvhomepgelements.searchresult);
		CommonRepository.clickElementJscript(driver, common);
		CommonRepository.waitforPageLoad(driver);
		fvhomepage.getElement(Fvhomepage.Fvhomepgelements.enterredeemcode).sendKeys(token);
		Thread.sleep(3000);
		fvhomepage.getElement(Fvhomepage.Fvhomepgelements.redeemcontinue).click();
		CommonRepository.waitforPageLoad(driver);
		Thread.sleep(7000);
		redeempage.validRedeemcode();
        
		fvsignuppage.getElement(FVsignuppage.Fvsignuppgelements.clickhere).click();
		CommonRepository.waitforPageLoad(driver);
		Thread.sleep(2000);
		String timeStamp = commonRepository.getTimeStamp();
		String FVEmailAddress ="test_FV_" + timeStamp +"@mailinator.com";
		fvsignuppage.getElement(Fvsignuppgelements.EmailAddress).sendKeys(FVEmailAddress);
		
		Thread.sleep(3000);
		fvsignuppage.FVSignupForm();
		commonRepository.scrollDown(2, driver);
		fvsignuppage.getElement(FVsignuppage.Fvsignuppgelements.SameUVoption).click();
		Thread.sleep(2000);
		fvsignuppage.getElement(FVsignuppage.Fvsignuppgelements.continuebutton).click();
		Thread.sleep(20000);
		CommonRepository.waitforPageLoad(driver);
		redeempage.checkOptin();
		Thread.sleep(6000);
		redeempage.getElement(Redeempage.Redeempgelements.completeredemption).click();
		CommonRepository.waitforPageLoad(driver);
		Thread.sleep(4000);
		redeempage.validateTitleRedemption();
		redeempage.validateRegistratrionredemption();
	}
}
