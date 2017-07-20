package pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonRepository{

	public static void getFrames(WebDriver driver) {
		List<WebElement> frames = driver.findElements(By.tagName("iframe"));
		System.out.println("Number of Frames are-->" + frames.size());
		for (WebElement frameid : frames) {
			System.out.println(frameid);
		}
	}

	public static void clickElementJscript(WebDriver driver, WebElement common) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", common);
	}

	public static void waitforPageLoad(WebDriver driver) {
		/**
		 * This loop will rotate for 100 times to check If page Is ready after
		 * every 1 second given one sec wait time for every iteration. loop
		 * executes until the document load status is complete.
		 * 
		 * @author 416467
		 */
		for (int i = 0; i < 500; i++) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.out.println("Error page not loaded properly");
			}
			// Check for page ready state.
			if (((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
					.equals("complete")) {
				break;
			}
		}
	}

	public String getTimeStamp() throws InterruptedException {
		String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(Calendar.getInstance().getTime());
		Thread.sleep(2000);
		return timeStamp;
	}

	public static void scrollDown(int n, WebDriver driver) {
		try {
			for (int i = 0;; i++) {
				if (i >= n) {
					break;
				}
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0,400)", "");
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
