package hepsiburadaQA;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginCase extends BaseTest {

    public AndroidDriver<MobileElement> driver;
    public WebDriverWait wait;

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
        dCaps.setCapability("noReset", "false");
        dCaps.setCapability("autoGrantPermissions", "true");

        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), dCaps);
        wait = new WebDriverWait(driver, 10);
    }

    @Override
    protected AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    @Test
    public void login() {
        // Close that little animation
        wait.until(ExpectedConditions.elementToBeClickable(By.id("close_button"))).click();
        // Account icon pressed
        wait.until(ExpectedConditions.elementToBeClickable(By.id("account_icon"))).click();
        // Login sequence started by pressing login button
        wait.until(ExpectedConditions.elementToBeClickable(By.id("llUserAccountLogin"))).click();
        // Email field selected and sent email info
        wait.until(ExpectedConditions.elementToBeClickable(By.id("etLoginEmail"))).sendKeys("enamtest1@gmail.com");
        // Password field selected and sent password info
        wait.until(ExpectedConditions.elementToBeClickable(By.id("etLoginPassword"))).sendKeys("E7KRGC*1993a");
        // Login button pressed
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLoginLogin"))).click();
        //TODO fix by reaching id of Close button, dialog popup
        wait.until(ExpectedConditions.elementToBeClickable(By.className("android.widget.Button"))).click();
        // Account button selected
        wait.until(ExpectedConditions.elementToBeClickable(By.id("tvUserAccountUsername"))).click();
        // Name field selected, cleared and sent updated name info
        WebElement firstName = wait.until(ExpectedConditions.elementToBeClickable(By.id("etUserFirstName")));
        firstName.clear();
        firstName.sendKeys("EnamK");

        // Update profile, extra control for hidden element, screens can be very sm0ll :)
        boolean isFoundElement = driver.findElements(By.id("btnOkSend")).size() > 0;
        while (!isFoundElement) {
            scroll();
            isFoundElement = driver.findElements(By.id("btnOkSend")).size() > 0;
        }
        driver.findElement(By.id("btnOkSend")).click();
        //TODO fix by reaching id of Close button, dialog popup
        wait.until(ExpectedConditions.elementToBeClickable(By.className("android.widget.Button"))).click();
    }

    @AfterMethod
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception exception) {
        }
    }
}
