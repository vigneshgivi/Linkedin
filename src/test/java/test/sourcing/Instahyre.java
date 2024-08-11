package testl.inkedIn;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import utility.ExcelUtility;
import utility.Waits;

public class Instahyre extends Appbase {
	private WebDriverWait wait;
	Waits normalWait;

	private Map<String, String> value;

	public Instahyre() {
		normalWait = new Waits();
	}

	@Test(priority = 1)
	public void login() {

		// Initialize WebDriverWait
		wait = new WebDriverWait(getDriver(), Duration.ofSeconds(40));

		// Navigate to the Instahyre login page
		getDriver().get("https://www.instahyre.com/login/?next=/candidate/opportunities/%3Fmatching%3Dtrue");
		System.out.println("Navigated to Instahyre login page.");
		normalWait.normalwait(5000);
		// Wait for the page to load
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
		System.out.println("Waited for page to load.");
		// Enter the username
		WebElement usernameField = getDriver().findElement(By.id("email"));
		usernameField.sendKeys("vigneshgivi27@gmail.com");
		System.out.println("Entered username.");

		// Enter the password
		WebElement passwordField = getDriver().findElement(By.id("password"));
		passwordField.sendKeys("Instahyre@5040");
		System.out.println("Entered password.");

		// Click the login button
		WebElement loginButton = getDriver().findElement(By.xpath("//button[text()='Login']"));
		loginButton.click();
		System.out.println("Clicked login button.");

		// Wait for the login process to complete
		wait.until(ExpectedConditions.urlContains("/candidate/opportunities"));
		System.out.println("Login process completed.");

	}

	@Test(priority = 2, dependsOnMethods = "login")
	public void processOpportunities() {

		boolean morePages = true;
		value = new LinkedHashMap<>();
		int counter = 1;

		while (morePages) {

			System.out.println("Processing a new page...");

			List<WebElement> opportunities = wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//button[@id=\"interested-btn\"]")));

			System.out.println("all Element found : " + opportunities.isEmpty());

			for (WebElement opportunity : opportunities) {

				try {
					((JavascriptExecutor) getDriver())
							.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", opportunity);
					normalWait.normalwait(2000);
				} catch (Exception e) {
					((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", opportunity);
					((JavascriptExecutor) getDriver())
							.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", opportunity);
					normalWait.normalwait(2000);

				}

				String recruiterName = wait
						.until(ExpectedConditions
								.presenceOfElementLocated(By.xpath("//span[@class=\"rec-name ng-binding\"]")))
						.getText();

				String recruiterCompany = wait
						.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath("//span[@class=\"designation ng-binding\"]/span[@class=\"ng-binding\"]")))
						.getText();
				normalWait.normalwait(2000);
				getDriver().findElement(By.xpath("//div[@class=\"application-modal-close back-button-modal-close\"]"))
						.click();

				System.out.println(recruiterName + "  : " + recruiterCompany);

				value.put(counter + "_" + recruiterName, recruiterCompany);
				counter++;
			}

			// Check if the "Next" button is available and click it if present
			try {
				WebElement nextButton = getDriver()
						.findElement(By.xpath("(//div[@class=\"pagination ng-scope\"]/li)[last()]"));
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", nextButton);

				if (nextButton.isDisplayed()) {
					nextButton.click();
					morePages = true;
					System.out.println("Next button clicked :" + morePages);
					normalWait.normalwait(6000);

				} else {
					morePages = false; // No more pages
					System.out.println("No \"Next\" button found or other issues : " + morePages);
				}
			} catch (Exception e) {
				morePages = false;
			}
		}
		

		if (!value.isEmpty()) {
			ExcelUtility.writeKeyValuePairsToExcel(value, "InstahyreJobPostingInformation", "Instahyre_HR_Profile_");
		} else {
			System.out.println("No data to write.");
		}
	}
}
