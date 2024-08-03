package testl.inkedIn;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class ExtractInformation extends Appbase {

	public ExtractInformation() {

	}

	@Test(priority = 1)
	public void login() {

		driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("vigneshvasugv01@gmail.com");
		driver.findElement(By.xpath("//input[@id=\"password\"]")).sendKeys("Linkedin@user@10");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

	}

	@Test(priority = 2)
	public void ExtractProfile() throws AWTException {

		driver.findElement(By.xpath("//span[text()=\"Jobs\"]")).click();
		WebElement element = driver.findElement(By.xpath("//input[contains(@id,\"jobs-search-box-keyword-id\")]"));

		element.sendKeys("Test Engineer");
			
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);

		driver.findElement(By.xpath("//button[text()=\"Date posted\"]")).click();
		
		WebElement mouseHoverToPast24hours=driver.findElement(By.xpath("//span[text()=\"Past 24 hours\"]"));
		
		Actions actions = new Actions(driver);
		
		actions.moveToElement(mouseHoverToPast24hours).click();
		
		WebElement mouseHoverToShowResult=driver.findElement(By.xpath("((//*[@class=\"reusable-search-filters-trigger-dropdown__container\"])//button)[2]"));
		actions.moveToElement(mouseHoverToShowResult).click();
		
		// experiance level filter
		driver.findElement(By.xpath("//button[@id=\"searchFilter_experience\"]")).click();
		driver.findElement(By.xpath("(//span[text()=\"Entry level\"])[1]")).click();
		driver.findElement(By.xpath("(//span[text()=\"Associate\"])[1]")).click();
		driver.findElement(By.xpath("//button[@id=\"ember1122\"]")).click();
		driver.findElement(By.xpath("//button[(text()=\"Easy Apply\")]")).click();

		System.out.println("Extracted : All Profile");
	}
}
