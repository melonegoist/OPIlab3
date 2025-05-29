package ru.melon_egoist.auth.pages;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    @Order(1)
    public void testRegistration() {
        driver.get("http://localhost:5173/");

        driver.findElement(By.id("login-field")).sendKeys("admin");
        driver.findElement(By.id("password-field")).sendKeys("password111");
        driver.findElement(By.id("login-button")).click();

        WebElement error = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(By.id("errorMessage")));

        Assertions.assertTrue(error.isDisplayed());
    }

}
