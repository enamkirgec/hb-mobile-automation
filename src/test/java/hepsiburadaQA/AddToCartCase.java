package hepsiburadaQA;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class AddToCartCase extends BaseTest {

    public WebDriverWait wait;
    private AndroidDriver<MobileElement> driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dCaps = new DesiredCapabilities();
        //TODO define global desired capabilities
        dCaps.setCapability("deviceName", "Galaxy Note8");
        dCaps.setCapability("platformName", "Android");
        dCaps.setCapability("udid", "ce061716e2ac4f26017e");
        dCaps.setCapability("platformVersion", "9.0");
        dCaps.setCapability("appPackage", "com.pozitron.hepsiburada");
        dCaps.setCapability("appActivity", "com.hepsiburada.launcher.DefaultLauncherAlias");
        dCaps.setCapability("noReset", "true");
        dCaps.setCapability("autoGrantPermissions", "true");

        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), dCaps);
        wait = new WebDriverWait(driver, 10);
    }

    // In order to use appium driver scroll/swipe
    @Override
    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @Test
    public void AddToCart() {

        //TODO Close that little animation, if it appears
        //wait.until(ExpectedConditions.elementToBeClickable(By.id("close_button"))).click();

        boolean isFoundElement = driver.findElements(By.id("dod_all")).size() > 0;
        while (!isFoundElement) {
            scroll();
            isFoundElement = driver.findElements(By.id("dod_all")).size() > 0;
        }
        driver.findElement(By.id("dod_all")).click();

        // Click first item on the list of products
        wait.until(ExpectedConditions.elementToBeClickable(By.id("view_product"))).click();
        // Click product image
        wait.until(ExpectedConditions.elementToBeClickable(By.id("productImage"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("preLoadImage"))).click();

        for (int i = 0; i <= 1; i++) {
            // Next product photo
            scroll(ScrollDirection.RIGHT);
        }

        // Close product image
        driver.navigate().back();
        // Adding item to cart
        wait.until(ExpectedConditions.elementToBeClickable(By.id("product_detail_add_to_cart"))).click();
        // Close product detail
        driver.navigate().back();
        // Click "Sepet"
        wait.until(ExpectedConditions.elementToBeClickable(MobileBy.ByAccessibilityId.AccessibilityId("navigationItem_2"))).click();
    }

    @AfterMethod
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception exception) {
        }
    }
}