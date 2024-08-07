package testl.inkedIn;

import java.awt.AWTException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utility.ExcelUtility;

public class ExtractInformation {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;  // Declare Actions

    private Map<String, String> keyValuePairs;

    // LinkedIn credentials
    private String linkedinEmail = "vigneshgivi27@gmail.com";
    private String linkedinPassword = "Linkedin@0708#";

    // Naukri credentials
    private String naukriEmail = "vigneshgivi27@gmail.com";
    private String naukriPassword = "Naukri@27";

    @BeforeClass
    public void setUp() {
        // Set the path of the ChromeDriver
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Create an instance of the ChromeDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time for better handling of delays
        actions = new Actions(driver);  // Initialize Actions

        // Maximize the browser window
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void linkedinLoginAndFilter() throws InterruptedException, AWTException {
        driver.get("https://www.linkedin.com/login");
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys(linkedinEmail);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(linkedinPassword);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.navigate().to("https://www.linkedin.com/jobs/");

        Thread.sleep(10000);

        // Perform a search for "Test Engineer"
        WebElement searchBox = driver.findElement(By.xpath("(//input[contains(@id,'jobs-search-box-keyword-id')])"));
        searchBox.sendKeys("Test Engineer", Keys.ENTER);

        // Click on "Date posted" button
        WebElement datePostedButton = driver.findElement(By.xpath("//button[text()='Date posted']"));
        actions.moveToElement(datePostedButton).click().perform();
        Thread.sleep(5000);

        // Click on "Past 24 hours" filter
        WebElement past24HoursFilter = driver.findElement(By.xpath("//span[text()='Past 24 hours']"));
        actions.moveToElement(past24HoursFilter).click().perform();
        Thread.sleep(5000);

        // Click to show results
        WebElement showResultsButton = driver.findElement(By.xpath("((//*[@class='reusable-search-filters-trigger-dropdown__container'])//button)[2]"));
        actions.moveToElement(showResultsButton).click().perform();
        Thread.sleep(10000);

        // Click on experience filter
        WebElement experienceFilterButton = driver.findElement(By.xpath("//button[@id='searchFilter_experience']"));
        actions.moveToElement(experienceFilterButton).click().perform();
        Thread.sleep(10000);

        // Click on "Entry level" and "Associate" options
        WebElement entryLevelOption = driver.findElement(By.xpath("(//span[text()='Entry level'])[1]"));
        actions.moveToElement(entryLevelOption).click().perform();

        WebElement associateOption = driver.findElement(By.xpath("(//span[text()='Associate'])[1]"));
        actions.moveToElement(associateOption).click().perform();
        Thread.sleep(10000);

        // Apply the experience filter
        WebElement applyExperienceFilterButton = driver.findElement(By.xpath("(//button[contains(@aria-label,'Apply current filter to show')])[2]"));
        actions.moveToElement(applyExperienceFilterButton).click().perform();
        Thread.sleep(10000);

        // Click on "Easy Apply" button
        WebElement easyApplyButton = driver.findElement(By.xpath("//button[text()='Easy Apply']"));
        actions.moveToElement(easyApplyButton).click().perform();

        System.out.println("Select Filter Completed");

        extractLinkedInJobInfo();
    }

    private void extractLinkedInJobInfo() throws InterruptedException {
        boolean hasNextPage = true;
        keyValuePairs = new HashMap<>();
        while (hasNextPage) {
            Thread.sleep(10000);

            List<WebElement> scrollUpto = driver.findElements(By.xpath("(//li[contains(@class,'scaffold-layout__list-item')])"));

            for (WebElement scrollallselect : scrollUpto) {
                Thread.sleep(2000);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollallselect);
            }

            Thread.sleep(5000);

            List<WebElement> jobsTitleList = driver.findElements(By.xpath("//div[@class='full-width artdeco-entity-lockup__title ember-view']/a"));

            for (WebElement jobTitle : jobsTitleList) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", jobTitle);

                String getTitle = jobTitle.getAttribute("aria-label");
                System.out.println(getTitle);

                if (getTitle.contains("Quality Assurance") || getTitle.contains("QA") || getTitle.contains("Test") || getTitle.contains("Quality Test Engineer") || getTitle.contains("Performance Testing") || getTitle.contains("SDET") || getTitle.contains("Automation Engineer")) {
                    jobTitle.click();

                    List<WebElement> hiringTeam = driver.findElements(By.xpath("//div[@class='hirer-card__hirer-information']/a"));

                    if (hiringTeam.isEmpty()) {
                        System.out.println("Meet the hiring team not found");
                    } else {
                        String name = driver.findElement(By.xpath("//div[@class='hirer-card__hirer-information']/a")).getAttribute("aria-label");
                        String URL = driver.findElement(By.xpath("//div[@class='hirer-card__hirer-information']/a")).getAttribute("href");
                        System.out.println(name + " : " + URL);

                        keyValuePairs.put(name, URL);
                    }
                }
            }

            try {
                WebElement nextButton = driver.findElement(By.xpath("(//div[contains(@class,\"jobs-search-results-list__pagination\")]//li/button[@aria-current=\"true\"]/parent::li/following-sibling::li)[1]"));
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

    @Test(priority = 2)
    public void naukriLoginAndUpdateResumeHeadline() throws InterruptedException {
        driver.get("https://www.naukri.com/");
        
        WebElement loginButtonInitial = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Login')]")));
        loginButtonInitial.click();

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='form-row']/input)[1]")));
        usernameField.sendKeys(naukriEmail);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='form-row']/input)[2]")));
        passwordField.sendKeys(naukriPassword);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Login']")));
        loginButton.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-profile-wrapper']")));

        WebElement viewProfile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='view-profile-wrapper']")));
        viewProfile.click();

        try {
            WebElement cancelIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'crossIcon chatBot')]")));
            cancelIcon.click();
        } catch (Exception e) {
            System.out.println("Cancel icon not found or not clickable.");
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -500);"); // Adjust scroll amount as needed

        Thread.sleep(2000); // Sleep for 2 seconds

        WebElement editResumeHeadlineIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='edit icon'])[1]")));
        editResumeHeadlineIcon.click();

        Thread.sleep(2000); // Sleep for 2 seconds

        WebElement resumeHeadlineTextArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@id='resumeHeadlineTxt']")));
        String existingText = resumeHeadlineTextArea.getText().trim();

        System.out.println("Existing text: \"" + existingText + "\"");

        if (existingText.endsWith(".")) {
            existingText = existingText.substring(0, existingText.length() - 1); // Remove trailing period
            System.out.println("Removed trailing period.");
        } else {
            existingText += "."; // Add a period if not present
            System.out.println("Added trailing period.");
        }

        resumeHeadlineTextArea.clear();
        resumeHeadlineTextArea.sendKeys(existingText);

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Save']")));
        saveButton.click();

        System.out.println("Save button clicked.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
