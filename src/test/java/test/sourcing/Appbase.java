package testl.inkedIn;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class Appbase {

	// Use ThreadLocal for thread-safe WebDriver instances
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	// Provide a way to access the driver
	public static WebDriver getDriver() {
		return driver.get();
	}

	@BeforeClass
	public void setup() {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");

		// Initialize WebDriver for the current thread
		driver.set(new ChromeDriver(options));
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
	}

	@AfterClass
	public void tearDown() {
		// Quit WebDriver for the current thread
		getDriver().quit();
		driver.remove(); // Clean up after the test
	}

}
