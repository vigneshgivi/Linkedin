package testl.inkedIn;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class Appbase {

	static WebDriver driver;

	@BeforeClass
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
		driver.get("https://www.linkedin.com/login");
		driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys("vigneshvasugv01@gmail.com");
		driver.findElement(By.xpath("//input[@id=\"password\"]")).sendKeys("Linkedin@user@10");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		driver.navigate().to("https://www.linkedin.com/jobs/");
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
