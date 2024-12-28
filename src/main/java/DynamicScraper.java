import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicScraper {
    public static void main(String[] args) {
    	long[] time = new long[] { 0, System.currentTimeMillis() };
    	log(time, "Start");
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        log(time, "ChromeDriver");

        ChromeOptions options = new ChromeOptions();
        log(time, "Options");
        options.addArguments("--headless"); // Run in headless mode
        log(time, "Headless");
        options.addArguments("--disable-gpu"); // Disable GPU rendering
        log(time, "disableGPU");
        
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images", 2); // Disable images
        prefs.put("profile.managed_default_content_settings.fonts", 2); // Disable fonts
        options.setExperimentalOption("prefs", prefs);
        
        WebDriver driver = new ChromeDriver(options);
        log(time, "Driver");
        
        String baseUrl = "https://www.rottentomatoes.com/m/$MOVIE_ID/reviews";
        try {
        	for (String id : new String[] { "nosferatu_2024" }) {
	        	int[] ratings = getRatings(driver, baseUrl.replace("$MOVIE_ID", id));
	        	log(time, id);
	        	System.out.print(id + ": ");
	        	for (int i : ratings) {
	        		System.out.print(i + ", ");
	        	}
	        	System.out.println();
	        	System.out.println(ratings.length);
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
    
    private static int[] getRatings(WebDriver driver, String url) {
    	driver.get(url);
        for (int i = 0; i < 15; i++) {
            // Find and click the "Next" button
            try {
                WebElement nextButton = driver.findElement(By.cssSelector("rt-button[data-qa='load-more-btn']"));
                nextButton.click();
                Thread.sleep(2500);
            } catch (Exception e) {
                break; // No more pages
            }
        }
        List<WebElement> reviewElements = driver.findElements(By.cssSelector(".review-row"));
        int[] ret = new int[reviewElements.size()];
        for (int i = 0; i < ret.length; i++) {
        	ret[i] = reviewElements.get(ret.length - 1 - i).getText().contains("Fresh") ? 1 : 0;
        	System.out.println(reviewElements.get(ret.length - 1 - i).getText());
        }
        return ret;
    }
    
    private static void log(long[] time, String str) {
    	time[0] = time[1];
    	time[1] = System.currentTimeMillis();
    	System.out.println(str + ": " + (time[1] - time[0]));
    }
}
