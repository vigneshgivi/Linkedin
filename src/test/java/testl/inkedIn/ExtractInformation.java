package testl.inkedIn;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import utility.ExcelUtility;

public class ExtractInformation extends Appbase {

	private Map<String, String> keyValuePairs;

	private WebDriverWait wait;

	public ExtractInformation() {
		// Constructor
		wait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Adjust timeout as needed
	}

	String email = "vigneshgivi27@gmail.com";
	String password = "Linkedin@0708#";

	@Test(priority = 1)
	public void login() {
		// Login logic
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.navigate().to("https://www.linkedin.com/jobs/");
	}

	@Test(priority = 2)
	public void selectFilter() {
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//input[contains(@id,'jobs-search-box-keyword-id')]")))
				.sendKeys("Test Engineer", Keys.ENTER);

		Actions actions = new Actions(driver);
		driver.findElement(By.xpath("//button[text()='Date posted']")).click();

		WebElement mouseHoverToPast24hours = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Past 24 hours']")));
		actions.moveToElement(mouseHoverToPast24hours).click().build().perform();

		WebElement mouseHoverToShowResult = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("((//*[@class='reusable-search-filters-trigger-dropdown__container'])//button)[2]")));
		actions.moveToElement(mouseHoverToShowResult).click().build().perform();

		WebElement mouseHoverToexperienceFilter = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='searchFilter_experience']")));
		actions.moveToElement(mouseHoverToexperienceFilter).click().build().perform();

		WebElement mouseHoverToEntrylevel = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Entry level'])[1]")));
		actions.moveToElement(mouseHoverToEntrylevel).click().build().perform();

		WebElement mouseHoverToAssociate = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Associate'])[1]")));
		actions.moveToElement(mouseHoverToAssociate).click().build().perform();

		WebElement mouseHoverToexperienceFilterShowResult = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("(//button[contains(@aria-label,'Apply current filter to show')])[2]")));
		actions.moveToElement(mouseHoverToexperienceFilterShowResult).click().build().perform();

		WebElement mouseHoverToEasyApply = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Easy Apply']")));
		actions.moveToElement(mouseHoverToEasyApply).click().build().perform();

		System.out.println("Select Filter Completed");
	}

	@Test(priority = 3)
	public void extractList() {
		boolean hasNextPage = true;
		keyValuePairs = new HashMap<>();

		while (hasNextPage) {
			List<WebElement> scrollUpto = driver
					.findElements(By.xpath("(//li[contains(@class,'scaffold-layout__list-item')])"));
			for (WebElement scrollallselect : scrollUpto) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollallselect);
			}

			List<WebElement> jobsTitleList = driver
					.findElements(By.xpath("//div[@class='full-width artdeco-entity-lockup__title ember-view']/a"));
			for (WebElement jobTitle : jobsTitleList) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", jobTitle);

				String getTitle = jobTitle.getAttribute("aria-label");
				System.out.println(getTitle);

				if (getTitle.matches(
						"(?i).*Quality Assurance|QA|Test|Quality Test Engineer|Performance Testing|SDET|Automation Engineer.*")) {
					jobTitle.click();

					List<WebElement> hiringTeam = driver
							.findElements(By.xpath("//div[@class='hirer-card__hirer-information']/a"));

					if (hiringTeam.isEmpty()) {
						System.out.println("Meet the hiring team not found");
					} else {
						String name = hiringTeam.get(0).getAttribute("aria-label");
						String URL = hiringTeam.get(0).getAttribute("href");

						System.out.println(name + " : " + URL);
						keyValuePairs.put(name, URL);
					}
				}
			}

			try {
				WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"(//div[contains(@class,\"jobs-search-results-list__pagination\")]//li/button[@aria-current=\"true\"]/parent::li/following-sibling::li)[1]")));
				nextButton.click();
			} catch (Exception e) {
				hasNextPage = false;
				System.out.println("Extraction Completed");
			}
		}
		ExcelUtility.writeKeyValuePairsToExcel(keyValuePairs);
	}

}
