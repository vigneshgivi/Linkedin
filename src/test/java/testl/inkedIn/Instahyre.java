package testl.inkedIn;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import utility.Waits;

import java.time.Duration;
import java.util.List;

public class Instahyre extends Appbase {
	private WebDriverWait wait;
	Waits normalWait;

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
		try {
			// Wait for the opportunities page to load
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//div[@class='ng-scope']/button[contains(@class,'button-interested')]")));
			System.out.println("Opportunities page loaded.");

			boolean hasNextPage = true; // Assume there is a next page

			while (hasNextPage) {
				// Find all "Interested" buttons on the current page
				List<WebElement> interestedButtons = getDriver().findElements(
						By.xpath("//div[@class='ng-scope']/button[contains(@class,'button-interested')]"));

				if (interestedButtons.isEmpty()) {
					System.out.println("No 'Interested' buttons found on the current page.");
					break;
				}

				System.out.println("Found " + interestedButtons.size() + " 'Interested' buttons.");

				for (WebElement interestedButton : interestedButtons) {
					try {
						// Wait until the "Interested" button is visible and clickable
						WebElement clickableButton = wait
								.until(ExpectedConditions.elementToBeClickable(interestedButton));

						// Click the "Interested" button
						clickableButton.click();
						System.out.println("Clicked 'Interested' button.");

						// Wait for job details to load
						wait.until(
								ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='rec-info']")));
						System.out.println("Job details loaded.");

						// Read and print the job information
						WebElement infoSpan = getDriver().findElement(By.xpath("//span[@class='rec-info']"));
						System.out.println("Job Information: " + infoSpan.getText());

						// Close the job details window
						WebElement closeButton = getDriver().findElement(By.xpath("(//i[@class='fa fa-close'])[1]"));
						closeButton.click();
						System.out.println("Closed job details window.");

						// Wait for the "Interested" button to be available again
						wait.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath("//div[@class='ng-scope']/button[contains(@class,'button-interested')]")));
					} catch (Exception e) {
						System.out.println("Failed to process job details.");
						e.printStackTrace();
					}
				}

				// Attempt to go to the next page
				try {
					WebElement nextButton = getDriver().findElement(By.xpath(
							"(//div[contains(@class,'pagination')]//li[contains(@class,'ng-binding ng-scope active')]/following-sibling::li)[1]"));
					if (nextButton.isDisplayed()) {
						nextButton.click(); // Click the "Next" button
						System.out.println("Clicked 'Next' button.");
						wait.until(ExpectedConditions.stalenessOf(nextButton)); // Wait for the next page to load
					} else {
						System.out.println("No more pages. Extraction is completed.");
						hasNextPage = false; // Exit the loop if no more pages
					}
				} catch (Exception ex) {
					System.out.println("No more pages or error in finding the 'Next' button. Extraction is completed.");
					hasNextPage = false; // Exit the loop if the "Next" button is not found or an error occurs
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to process opportunities.");
			e.printStackTrace();
		}
	}
}
