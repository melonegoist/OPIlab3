package ru.melon_egoist.auth.pages;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTest {

    private WebDriver driver;

    @BeforeAll
    void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();

        driver.get("http://localhost:5173/");
//        login("admin", "1234");
    }

//    @BeforeEach
//    void openLoginPage() {
//        driver.get("http://localhost:5173/");
//        login("admin", "1234");
//    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    @Test
    @Order(1)
    void redirectToRegisterPage() {
        driver.findElement(By.id("corner_button")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("/reg"));
    }

    @Test
    @Order(2)
    void togglePasswordVisibility() {
        WebElement passwordField = driver.findElement(By.id("password-field"));
        passwordField.sendKeys("secret_password");
        driver.findElement(By.id("lock-icon")).click();
        assertEquals("text", passwordField.getAttribute("type"));
    }

    @Test
    @Order(3)
    void successfulLoginDrawsGraph() {
//        login("admin", "1234");
        WebElement canvas = driver.findElement(By.id("graph"));
        Assertions.assertTrue(canvas.isDisplayed());
    }

    @Test
    @Order(3)
    void logoutClearsSession() {
//        login("admin", "1234");
        driver.findElement(By.id("corner_button")).click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("/"));
    }

    @Test
    @Order(4)
    void usernameMessageAppears() {
        driver.findElement(By.id("corner_button")).click();
        login("admin", "1234");
        Assertions.assertTrue(driver.getPageSource().contains("Welcome, admin"));
    }

    @Test
    @Order(5)
    void emptyXShowsError() {
        login("admin", "1234");

        WebElement xInput = driver.findElement(By.id("x-input"));
        xInput.clear();
        assertEquals("", xInput.getAttribute("value"));

        // Указываем Y и R, чтобы проверить только поведение пустого X
        driver.findElement(By.id("y-input")).sendKeys("2");
        driver.findElement(By.id("r-input")).sendKeys("3");

        // Нажимаем кнопку "Send"
        WebElement sendButton = driver.findElement(By.id("submit-button"));
        sendButton.click();

        WebElement notAllowedGraph = driver.findElement(By.id("graph"));
        Assertions.assertFalse(notAllowedGraph.isDisplayed());
    }

    @Test
    @Order(6)
    void emptyYShowsError() {
        WebElement yInput = driver.findElement(By.id("y-input"));
        yInput.clear();
        assertEquals("", yInput.getAttribute("value"));

        // Указываем Y и R, чтобы проверить только поведение пустого X
        driver.findElement(By.id("x-input")).sendKeys("1");
        driver.findElement(By.id("r-input")).sendKeys("3");

        // Нажимаем кнопку "Send"
        WebElement sendButton = driver.findElement(By.id("submit-button"));
        sendButton.click();

        WebElement notAllowedGraph = driver.findElement(By.id("graph"));
        Assertions.assertFalse(notAllowedGraph.isDisplayed());
    }

    @Test
    @Order(7)
    void emptyRShowsError() {
        // Указываем Y и R, чтобы проверить только поведение пустого X
        driver.findElement(By.id("x-input")).sendKeys("2");
        driver.findElement(By.id("y-input")).sendKeys("3");

        // Нажимаем кнопку "Send"
        WebElement sendButton = driver.findElement(By.id("submit-button"));
        sendButton.click();

        WebElement notAllowedGraph = driver.findElement(By.id("graph"));
        Assertions.assertFalse(notAllowedGraph.isDisplayed());
    }

    @Test
    @Order(8)
    void xOutsideRangeShowsError() {
        driver.findElement(By.id("x-input")).sendKeys("4"); // допустим [-3;3]
        driver.findElement(By.id("submit")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Wrong x"));
    }

    @Test
    @Order(9)
    void yOutsideRangeShowsError() {
        driver.findElement(By.id("y-input")).sendKeys("6"); // допустим [-2;5]
        driver.findElement(By.id("submit-button")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Wrong y"));
    }

    @Test
    @Order(10)
    void rOutsideRangeShowsError() {
        driver.findElement(By.id("r-input")).sendKeys("6"); // допустим [1;5]
        driver.findElement(By.id("submit-button")).click();
        Assertions.assertTrue(driver.getPageSource().contains("Wrong r"));
    }

    @Test
    @Order(11)
    void validDataDrawsGraph() {
        fillPointForm("1", "2", "3");
        WebElement graph = driver.findElement(By.id("graph-canvas"));
        Assertions.assertTrue(graph.isDisplayed());
    }

    @Test
    @Order(12)
    void showAllDotsDisplaysPreviousHits() {
//        login("admin", "1234");
        driver.findElement(By.id("show_dots_button")).click();
        WebElement table = driver.findElement(By.id("dashboard"));
        Assertions.assertTrue(table.isDisplayed());
    }

    @Test
    @Order(13)
    void sessionPersistsAfterReload() {
//        login("admin", "1234");
        driver.navigate().refresh();
        Assertions.assertTrue(driver.findElement(By.id("corner_button")).isDisplayed());
    }

    @Test
    @Order(14)
    void sendButtonColorChanges() {
        fillPointForm("1", "2", "3");
        WebElement button = driver.findElement(By.id("submit-button"));
        String color = button.getCssValue("background-color");
        // Проверка по цвету — зависит от CSS, просто пример:
        Assertions.assertTrue(color.contains("rgba(0, 128, 0") || color.contains("rgba(255, 0, 0"));
    }

    private void login(String username, String password) {
        driver.findElement(By.id("login-field")).sendKeys(username);
        driver.findElement(By.id("password-field")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    private void fillPointForm(String x, String y, String r) {
        login("testuser", "password123");
        driver.findElement(By.id("x-input")).sendKeys(x);
        driver.findElement(By.id("y-input")).sendKeys(y);
        driver.findElement(By.id("r-input")).sendKeys(r);
        driver.findElement(By.id("submit-button")).click();
    }
}
