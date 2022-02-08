import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class MyJunit {

    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
    }


    @Test
    public void getTitle() {
        driver.get("https://demoqa.com");
        String title = driver.getTitle();
        System.out.println(title);
    }

    @Test
    public void checkIfElementExists() {
        driver.get("https://demoqa.com");
        boolean status = driver.findElement(By.className("banner-image")).isDisplayed();
        Assert.assertTrue(status);
    }

    @Test
    public void fillUpTextBox() throws InterruptedException {
        driver.get("https://demoqa.com/text-box");
        wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("userForm"))).isDisplayed();
        driver.findElement(By.id("userName")).sendKeys("Jon Doe");
        driver.findElement(By.id("userEmail")).sendKeys("abc@text.com");
        driver.findElement(By.id("currentAddress")).sendKeys("32 Hollow st. Hellkitchen, New York");
        driver.findElement(By.id("permanentAddress")).sendKeys("32 Hollow st. Hellkitchen, New York");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[@id='submit']")).click();
    }

    @Test
    public void fillUpForm()
    {
        driver.get("https://demoqa.com/text-box");
        driver.findElement(By.id("userName")).sendKeys("Mr. Karim");
        driver.findElement(By.id("userEmail")).sendKeys("karim@test.com");
        driver.findElement(By.id("submit")).click();
    }

    @Test
    public void clickButton() {
        driver.get("https://demoqa.com/buttons");
        WebElement doubleClick = driver.findElement(By.id("doubleClickBtn"));
        WebElement rightClick = driver.findElement(By.id("rightClickBtn"));
        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClick).perform();
        actions.contextClick(rightClick).perform();

        String text1 = driver.findElement(By.id("doubleClickMessage")).getText();
        String text2 = driver.findElement(By.id("rightClickMessage")).getText();

        Assert.assertTrue(text1.contains("a double click"));
        Assert.assertTrue(text2.contains("a right click"));
    }

    @Test
    public void clickMultipleButton() throws InterruptedException {
        driver.get("https://demoqa.com/buttons");
        Thread.sleep(5000);
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        Actions actions = new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.doubleClick(buttons.get(2)).perform();
        actions.doubleClick(buttons.get(3)).perform();
        Thread.sleep(2500);
    }

    @Test
    public void alerts() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        driver.switchTo().alert().accept();
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();
        driver.findElement(By.id("promtButton")).click();
        Thread.sleep(2000);
        driver.switchTo().alert().sendKeys("Jon Doe");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

        String text = driver.findElement(By.id("promptResult")).getText();
        System.out.println(text);
        Assert.assertTrue(text.contains("You entered Jon Doe"));
    }

    @Test
    public void datePicker() throws InterruptedException {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("dateAndTimePickerInput")).clear();
        Thread.sleep(1500);
        driver.findElement(By.id("dateAndTimePickerInput")).sendKeys("05/08/1993 2:10 PM");
        driver.findElement(By.id("dateAndTimePickerInput")).sendKeys(Keys.ENTER);
    }

    @Test
    public void selectDropDowns() {
        driver.get("https://demoqa.com/select-menu");
        Select color = new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("4");
        Select cars = new Select(driver.findElement(By.id("cars")));
        if (cars.isMultiple()) {
            cars.selectByValue("volvo");
            cars.selectByValue("audi");
        }
    }

    @Test
    public void handleNewTab() throws InterruptedException {
        driver.get("https://demoqa.com/links");
        driver.findElement(By.id("simpleLink")).click();
        Thread.sleep(5000);
        ArrayList<String> w = new ArrayList<String>(driver.getWindowHandles());

        //switch to new tab
        driver.switchTo().window(w.get(1));
        String title = driver.getTitle();
        System.out.println("New tab title: " + title);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        Boolean status = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//img[@src='/images/Toolsqa.jpg']"))).isDisplayed();
        Assert.assertEquals(true, status);
        driver.close();
        driver.switchTo().window(w.get(0));
    }

    @Test
    public void handleNewWindow() {
        driver.get("https://demoqa.com/browser-windows");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("windowButton")));
        driver.findElement(By.id(("windowButton"))).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
                driver.close();
            }
        }
        driver.switchTo().window(mainWindowHandle);
    }

    @Test
    public void modalDialog() throws InterruptedException {
        driver.get("https://demoqa.com/modal-dialogs");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("showSmallModal")));
        element.click();
        Thread.sleep(1500);
        driver.findElement(By.id("closeSmallModal")).click();
    }
    @Test
    public void editTable() throws InterruptedException {
        driver.get("https://demoqa.com/webtables");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-record-1")));
        driver.findElement(By.id("edit-record-1")).click();
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("Jon");
        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys("Doe");
        Thread.sleep(1500);
        driver.findElement(By.id("submit")).click();

        driver.findElement(By.id("searchBox")).sendKeys("41");
        driver.findElement(By.className("input-group-text")).click();
        Thread.sleep(1500);
    }

    @Test
    public void scrapData() {
        driver.get("https://demoqa.com/webtables");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rt-tbody"))).isDisplayed();
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num[" + i + "] " + cell.getText());
            }
        }
    }

    @Test
    public void handleIframe() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println(text);
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("https://green.edu.bd");
        WebElement mainMenu = driver.findElement(By.xpath("//a[@class='dropdown-toggle'][contains(text(),'About Us')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(mainMenu).perform();
        Thread.sleep(1500);
        WebElement subMenu = driver.findElement(By.xpath("//li[@id='menu-item-325']//a[contains(text(),'History')]"));
        actions.moveToElement(subMenu);
        actions.click().build().perform();
        boolean status = driver.findElement(By.cssSelector("img[class='alignnone wp-image-387']")).isDisplayed();
        Assert.assertTrue(status);
        Thread.sleep(1500);
    }

    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("This is a Junit project")
                .keyUp(Keys.SHIFT)
                .doubleClick()
                .contextClick()
                .perform();
        Thread.sleep(2000);
    }

    @After
    public void closeBrowser() {
        driver.close();
    }
}




