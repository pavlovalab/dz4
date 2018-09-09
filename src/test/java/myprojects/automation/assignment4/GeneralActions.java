package myprojects.automation.assignment4;


import com.beust.jcommander.internal.Nullable;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private String browser;

    public GeneralActions(WebDriver driver, String browserName) {
        this.driver = driver;
        this.browser = browserName;
        wait = new WebDriverWait(driver, 30);
    }

    public void navigate(String url) {
        driver.navigate().to(url);
        waitForContentLoad();
    }

    /**
     * Logs in to Admin Panel.
     *
     * @param login
     * @param password
     */
    public void login(String login, String password) throws InterruptedException {
        navigate(Properties.getBaseAdminUrl());

        CustomReporter.log("Авторизация");

        driver.findElement(By.id("email")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).submit();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData newProduct) throws InterruptedException {

        waitForContentLoad();

        CustomReporter.log("Создание нового продукта");
        WebElement catalog = driver.findElement(By.cssSelector("#subtab-AdminCatalog[data-submenu='9']"));

        Actions builder = new Actions(driver);
        if (browser.equals("ie")) {
            builder.moveToElement(catalog, 0, -30).build().perform();
        } else {
            builder.moveToElement(catalog).build().perform();
        }

        // Ожидание появления подменю
        new WebDriverWait(driver, 1).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#subtab-AdminCatalog[class='maintab  has_submenu hover']")));
        WebElement category = driver.findElement(By.cssSelector("#subtab-AdminProducts[data-submenu='10']"));
        if (browser.equals("ie")) {
            builder.moveToElement(category, 0, -30).click().perform();
        } else {
            builder.moveToElement(category).click().perform();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));

        waitForContentLoad();

        WebElement button = driver.findElement(By.cssSelector(".m-b-2"));
        navigate(button.getAttribute("href"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form_step1_name_1")));

        driver.findElement(By.cssSelector("#form_step1_name_1")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.cssSelector("#form_step1_name_1")).sendKeys(newProduct.getName());
        driver.findElement(By.cssSelector("#form_step1_qty_0_shortcut")).sendKeys(newProduct.getQty().toString());
        driver.findElement(By.cssSelector("#form_step1_price_ttc_shortcut")).sendKeys(newProduct.getPrice());

        ((JavascriptExecutor)driver).executeScript("$('.switch-input').click();");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        WebElement mess = driver.findElement(By.className("growl-message"));
        ((JavascriptExecutor)driver).executeScript("$('.growl-close').click();");
        wait.until(ExpectedConditions.stalenessOf(mess));

        ((JavascriptExecutor)driver).executeScript("$('#submit').click();");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        mess = driver.findElement(By.className("growl-message"));
        ((JavascriptExecutor)driver).executeScript("$('.growl-close').click();");
        wait.until(ExpectedConditions.stalenessOf(mess));
    }

    public void checkProduct(ProductData newProduct) throws InterruptedException {

        navigate(Properties.getBaseUrl());

        CustomReporter.log("Проверка появления нового продукта на сайте");

        WebElement all_button = driver.findElement(By.className("all-product-link"));
        String next_href1 = all_button.getAttribute("href");
        navigate(next_href1);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("category")));

        boolean flag = false;
        WebElement item = null;

        do {
            List<WebElement> rows = driver.findElements(By.cssSelector(".product-description>h1>a"));
            Iterator<WebElement> iter = rows.iterator();


            while (iter.hasNext()) {
                item = iter.next();
                String label = item.getText().toUpperCase();
                if (label.equals(newProduct.getName().toUpperCase())) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) break;
            WebElement next_button = driver.findElement(By.cssSelector(".page-list>li>.next"));
            String href2 = driver.findElement(By.cssSelector(".page-list>li>.next")).getAttribute("href");

            if (next_href1.equals(href2)) break;
            if (!next_button.isEnabled()) break;
            next_href1 = href2;
            navigate(href2);
        }
        while (1 == 1);
        assertTrue (flag, "Элемент не найден");

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));

        assertEquals(driver.findElement(By.cssSelector(".row>div>h1")).getText().toUpperCase(), newProduct.getName().toUpperCase(), "Название продукта не соответствует");
        assertTrue(driver.findElement(By.cssSelector(".current-price>span")).getText().contains(newProduct.getPrice()), "Не соответствует цена продукта");
        assertTrue(driver.findElement(By.cssSelector(".product-quantities>span")).getText().contains(newProduct.getQty().toString()), "Не соответствует количество продукта");

        CustomReporter.log("Тест выполнен успешно");
    }
        /**
          * Waits until page loader disappears from the page
          */
    public void waitForContentLoad() {

        wait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
