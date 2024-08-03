/*package testl.inkedIn;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Chatgpt {

    private WebDriver driver;
    private WebDriverWait wait;
    private List<String> jobPosterNames = new ArrayList<>();

    @BeforeClass
    public void setUp() {
        // Set the path of the ChromeDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Create an instance of the ChromeDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Maximize the browser window
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void login() {
        // Open LinkedIn login page
        driver.get("https://www.linkedin.com/login");

        // Log in to LinkedIn
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.sendKeys("vigneshvasugv01@gmail.com");

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Linkedin@user@10");

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
    }

    @Test(priority = 2)
    public void searchAndFilterJobs() {
        // Wait for the page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));

        // Search for "Test Engineer" roles
        WebElement searchField = driver.findElement(By.xpath("//input[@placeholder='Search']"));
        searchField.sendKeys("Test Engineer");
        searchField.submit();

        // Apply filters: Date Posted -> Past 24 hours
        WebElement datePostedFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Date Posted filter')]")));
        datePostedFilter.click();
        WebElement past24HoursOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Past 24 hours']")));
        past24HoursOption.click();

        // Apply filters: Experience Level -> Entry level, Associate
        WebElement experienceLevelFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Experience Level filter')]")));
        experienceLevelFilter.click();
        WebElement entryLevelOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Entry level']")));
        entryLevelOption.click();
        WebElement associateOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='Associate']")));
        associateOption.click();
        WebElement showResultsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-control-name='all_filters_apply']")));
        showResultsButton.click();

        // Click on the Easy Apply button
        WebElement easyApplyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Easy Apply filter')]")));
        easyApplyButton.click();
    }

    @Test(priority = 3)
    public void extractJobPosters() {
        boolean hasNextPage = true;
        int jobPostingCount = 0;

        while (hasNextPage) {
            // Get the list of job postings
            List<WebElement> jobPostings = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@class='jobs-search__results-list']/li")));

            // Iterate through each job posting
            for (WebElement jobPosting : jobPostings) {
                WebElement jobTitleElement = jobPosting.findElement(By.xpath(".//h3"));
                String jobTitle = jobTitleElement.getText().toLowerCase();

                // Check if the job title contains the specified keywords
                if (jobTitle.contains("test") || jobTitle.contains("qa") || jobTitle.contains("sdet") ||
                    jobTitle.contains("quality assurance") || jobTitle.contains("automation")) {
                    jobTitleElement.click();

                    // Wait for the job details to load
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Meet the hiring team']")));

                    // Check if the "Meet the hiring team" text is displayed
                    List<WebElement> hiringTeamElements = driver.findElements(By.xpath("//*[text()='Meet the hiring team']"));
                    if (!hiringTeamElements.isEmpty()) {
                        WebElement hiringTeamElement = hiringTeamElements.get(0);
                        WebElement jobPosterElement = hiringTeamElement.findElement(By.xpath("../../following-sibling::div//h3"));
                        String jobPosterName = jobPosterElement.getText();
                        jobPosterNames.add(jobPosterName);
                        System.out.println("Job Poster: " + jobPosterName);
                    }
                }

                jobPostingCount++;

                // If 25 job postings have been processed, click the next page button
                if (jobPostingCount % 25 == 0) {
                    WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Next']")));
                    nextPageButton.click();
                    // Wait for the next page to load
                    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@class='jobs-search__results-list']/li")));
                }
            }

            // Check if there is a next page
            try {
                WebElement nextPageButton = driver.findElement(By.xpath("//button[@aria-label='Next']"));
                hasNextPage = nextPageButton.isDisplayed();
            } catch (NoSuchElementException e) {
                hasNextPage = false;
            }
        }
    }

    @AfterClass
    public void tearDown() {
        // Write job poster names to an Excel file
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Job Posters");

            int rowNum = 0;
            for (String jobPosterName : jobPosterNames) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(jobPosterName);
            }

            try (FileOutputStream fileOut = new FileOutputStream("JobPosters.xlsx")) {
                workbook.write(fileOut);
            }

            System.out.println("Job poster names have been written to JobPosters.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
*/