package testl.inkedIn;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Naukri extends Appbase {

	private WebDriverWait wait;

	private String naukriEmail = "vigneshgivi27@gmail.com";
	private String naukriPassword = "Naukri@27";

	public Naukri() {

	}

	@Test(priority = 1)
	public void naukriLoginAndUpdateResumeHeadline() {

		getDriver().get("https://www.naukri.com/");

		wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));

		WebElement loginButtonInitial = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Login')]")));
		loginButtonInitial.click();

		WebElement usernameField = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='form-row']/input)[1]")));
		usernameField.sendKeys(naukriEmail);

		WebElement passwordField = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='form-row']/input)[2]")));
		passwordField.sendKeys(naukriPassword);

		WebElement loginButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Login']")));
		loginButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-profile-wrapper']")));

		WebElement viewProfile = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='view-profile-wrapper']")));
		viewProfile.click();

		try {
			WebElement cancelIcon = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'crossIcon chatBot')]")));
			cancelIcon.click();
		} catch (Exception e) {
			System.out.println("Cancel icon not found or not clickable.");
		}

		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollBy(0, -500);");

		WebElement editResumeHeadlineIcon = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='edit icon'])[1]")));
		editResumeHeadlineIcon.click();

		WebElement resumeHeadlineTextArea = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@id='resumeHeadlineTxt']")));
		String existingText = resumeHeadlineTextArea.getText().trim();

		System.out.println("Existing text: \"" + existingText + "\"");

		if (existingText.endsWith(".")) {
			existingText = existingText.substring(0, existingText.length() - 1);
			System.out.println("Removed trailing period.");
		} else {
			existingText += ".";
			System.out.println("Added trailing period.");
		}

		resumeHeadlineTextArea.clear();
		resumeHeadlineTextArea.sendKeys(existingText);

		WebElement saveButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Save']")));
		saveButton.click();

		System.out.println("Save button clicked.");
	}
}
