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

import test.Utility.DataProviderUtil;
import utility.ExcelUtility;
import utility.Waits;

public class ExtractInformation extends Appbase {

	private Map<String, String> keyValuePairs;
	private WebDriverWait wait;
	Waits normalWait;

	public ExtractInformation() {
		normalWait = new Waits();
	}

//	String email = "vigneshvasugv01@gmail.com";
//	String password = "Linkedin@user@10";

	@Test(priority = 1, dataProvider = "Linkedlogindata", dataProviderClass = DataProviderUtil.class)
	public void login(String username, String password) {

		getDriver().get("https://www.linkedin.com/login");

		// Initialize WebDriverWait with the current thread's WebDriver instance
		wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60)); // Adjust timeout as needed

		// Login logic
		getDriver().findElement(By.xpath("//input[@id='username']")).sendKeys(username);
		getDriver().findElement(By.xpath("//input[@id='password']")).sendKeys(password);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();

		normalWait.normalwait(10000);

		getDriver().navigate().to("https://www.linkedin.com/jobs/");

	}

	@Test(priority = 2)
	public void selectFilter() {
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//input[contains(@id,'jobs-search-box-keyword-id')]")))
				.sendKeys("Test Engineer", Keys.ENTER);

		Actions actions = new Actions(getDriver());
		getDriver().findElement(By.xpath("//button[text()='Date posted']")).click();

		WebElement mouseHoverToPast24hours = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Past 24 hours']")));
		actions.moveToElement(mouseHoverToPast24hours).click().build().perform();

		WebElement mouseHoverToShowResult = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("((//*[@class='reusable-search-filters-trigger-dropdown__container'])//button)[2]")));
		actions.moveToElement(mouseHoverToShowResult).click().build().perform();

		normalWait.normalwait(5000);

		WebElement mouseHoverToexperienceFilter = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='searchFilter_experience']")));
		actions.moveToElement(mouseHoverToexperienceFilter).click().build().perform();

		normalWait.normalwait(2000);

		WebElement mouseHoverToEntrylevel = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Entry level'])[1]")));
		actions.moveToElement(mouseHoverToEntrylevel).click().build().perform();

		normalWait.normalwait(2000);

		WebElement mouseHoverToAssociate = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Associate'])[1]")));
		actions.moveToElement(mouseHoverToAssociate).click().build().perform();

		normalWait.normalwait(2000);

		WebElement mouseHoverToexperienceFilterShowResult = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("(//button[contains(@aria-label,'Apply current filter to show')])[2]")));
		actions.moveToElement(mouseHoverToexperienceFilterShowResult).click().build().perform();

		normalWait.normalwait(2000);

		WebElement mouseHoverToEasyApply = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Easy Apply']")));
		actions.moveToElement(mouseHoverToEasyApply).click().build().perform();

		normalWait.normalwait(2000);

		System.out.println("Select Filter Completed");
	}

	@Test(priority = 3)
	public void extractList() {
		boolean hasNextPage = true;
		keyValuePairs = new HashMap<>();

		while (hasNextPage) {
			List<WebElement> scrollUpto = getDriver()
					.findElements(By.xpath("(//li[contains(@class,'scaffold-layout__list-item')])"));
			for (WebElement scrollallselect : scrollUpto) {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", scrollallselect);
			}

			List<WebElement> jobsTitleList = getDriver()
					.findElements(By.xpath("//div[@class='full-width artdeco-entity-lockup__title ember-view']/a"));
			for (WebElement jobTitle : jobsTitleList) {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", jobTitle);

				String getTitle = jobTitle.getAttribute("aria-label");
				System.out.println(getTitle);

				if (getTitle.contains("Quality Assurance") || getTitle.contains("QA") || getTitle.contains("Test")
						|| getTitle.contains("Quality Test Engineer") || getTitle.contains("Performance Testing")
						|| getTitle.contains("SDET") || getTitle.contains("Automation Engineer")) {

					jobTitle.click();

					List<WebElement> hiringTeam = getDriver()
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
		if (!keyValuePairs.isEmpty()) {
			ExcelUtility.writeKeyValuePairsToExcel(keyValuePairs);
		} else {
			System.out.println("No data to write.");
		}
	}

}
