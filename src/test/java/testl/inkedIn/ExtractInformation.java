package testl.inkedIn;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import utility.ExcelUtility;

public class ExtractInformation extends Appbase {

	Map<String, String> keyValuePairs;

	public ExtractInformation() {
		// Constructor
	}

	@Test(priority = 1)
	public void login() throws InterruptedException {
		// Login logic if needed
		System.out.println("navigate to jobs done");
	}

	@Test(priority = 2)
	public void selectFilter() throws AWTException, InterruptedException {
		Thread.sleep(10000);
		driver.findElement(By.xpath("(//input[contains(@id,'jobs-search-box-keyword-id')])")).sendKeys("Test Engineer",
				Keys.ENTER);

		Actions actions = new Actions(driver);
		driver.findElement(By.xpath("//button[text()='Date posted']")).click();
		Thread.sleep(5000);

		WebElement mouseHoverToPast24hours = driver.findElement(By.xpath("//span[text()='Past 24 hours']"));
		actions.moveToElement(mouseHoverToPast24hours).click().build().perform();
		Thread.sleep(5000);

		WebElement mouseHoverToShowResult = driver.findElement(
				By.xpath("((//*[@class='reusable-search-filters-trigger-dropdown__container'])//button)[2]"));
		actions.moveToElement(mouseHoverToShowResult).click().build().perform();
		Thread.sleep(10000);

		WebElement mouseHoverToexperienceFilter = driver
				.findElement(By.xpath("//button[@id='searchFilter_experience']"));
		actions.moveToElement(mouseHoverToexperienceFilter).click().build().perform();
		Thread.sleep(10000);

		WebElement mouseHoverToEntrylevel = driver.findElement(By.xpath("(//span[text()='Entry level'])[1]"));
		actions.moveToElement(mouseHoverToEntrylevel).click().build().perform();

		WebElement mouseHoverToAssociate = driver.findElement(By.xpath("(//span[text()='Associate'])[1]"));
		actions.moveToElement(mouseHoverToAssociate).click().build().perform();
		Thread.sleep(10000);

		WebElement mouseHoverToexperienceFilterShowResult = driver
				.findElement(By.xpath("(//button[contains(@aria-label,'Apply current filter to show')])[2]"));
		actions.moveToElement(mouseHoverToexperienceFilterShowResult).click().build().perform();
		Thread.sleep(10000);

		WebElement mouseHoverToEasyApply = driver.findElement(By.xpath("//button[text()='Easy Apply']"));
		actions.moveToElement(mouseHoverToEasyApply).click().build().perform();
		Thread.sleep(10000);

		System.out.println("Select Filter Completed");
	}

	@Test(priority = 3)
	public void extractList() throws InterruptedException {
		boolean hasNextPage = true;
		keyValuePairs = new HashMap<>();

		while (hasNextPage) {
			Thread.sleep(10000);

			List<WebElement> scrollUpto = driver
					.findElements(By.xpath("(//li[contains(@class,'scaffold-layout__list-item')])"));

			for (WebElement scrollallselect : scrollUpto) {
				Thread.sleep(3000);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollallselect);
			}

			Thread.sleep(10000);

			List<WebElement> jobsTitleList = driver
					.findElements(By.xpath("//div[@class='full-width artdeco-entity-lockup__title ember-view']/a"));

			for (WebElement jobTitle : jobsTitleList) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", jobTitle);

				String getTitle = jobTitle.getAttribute("aria-label");
				System.out.println(getTitle);

				if (getTitle.contains("Quality Assurance") || getTitle.contains("QA") || getTitle.contains("Test")
						|| getTitle.contains("Quality Test Engineer") || getTitle.contains("Performance Testing")
						|| getTitle.contains("SDET") || getTitle.contains("Automation Engineer")) {
					jobTitle.click();

					List<WebElement> hiringTeam = driver
							.findElements(By.xpath("//div[@class='hirer-card__hirer-information']/a"));

					if (hiringTeam.isEmpty()) {
						System.out.println("Meet the hiring team not found");
					} else {
						String name = driver.findElement(By.xpath("//div[@class='hirer-card__hirer-information']/a"))
								.getAttribute("aria-label");
						String URL = driver.findElement(By.xpath("//div[@class='hirer-card__hirer-information']/a"))
								.getAttribute("href");

						System.out.println(name + " : " + URL);

						keyValuePairs.put(name, URL);
					}
				}
			}

			try {
				WebElement nextButton = driver.findElement(By.xpath(
						"(//div[contains(@class,\"jobs-search-results-list__pagination\")]//li/button[@aria-current=\"true\"]/parent::li/following-sibling::li)[1]"));
				nextButton.click(); // Click the "Next" button if it exists
				Thread.sleep(5000); // Wait for the next page to load
			} catch (NoSuchElementException e) {
				// No "Next" button found, end the loop
				hasNextPage = false;
				System.out.println("Extraction Completed");
			}

		}
		ExcelUtility.writeKeyValuePairsToExcel(keyValuePairs);
	}
}
// fulent wait