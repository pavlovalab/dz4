package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Iterator;
import java.util.List;
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

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        driver.navigate().to(Properties.getBaseAdminUrl());
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#email")));
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).submit();
//        button.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData newProduct) throws InterruptedException {
        WebElement catalog = driver.findElement(By.cssSelector("#subtab-AdminCatalog[data-submenu='9']"));
//        WebElement orders = driver.findElement(By.cssSelector("#subtab-AdminParentOrders[data-submenu='3']"));

        Actions builder = new Actions(driver);
        do {
            wait.until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
            //            wait.until(new Predicate<WebDriver>()
 //            {

//                 return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
//             }
 //            );
//            WebElement button = driver.findElement(By.cssSelector("#subtab-AdminProducts[data-submenu='10']"));
            // Ожидание появления подменю

//            System.out.println(((EventFiringWebDriver)driver).getClass().toString());
            if(browser.equals("ie")) {
                builder.moveToElement(catalog, 0, -30).perform();
            }else {
                builder.moveToElement(catalog).build().perform();
            }
            System.out.println(catalog.getAttribute("class"));

//            ((JavascriptExecutor) driver).executeScript("arguments[0].moveToElement(arguments[1]).perform();", builder, catalog);
//           System.out.println(catalog.getAttribute("class"));
//            Thread.sleep(2000);
//            button = driver.findElement(By.cssSelector("#subtab-AdminProducts[data-submenu='10']"));
            // Ожидание появления подменю

            try {
                //       Actions builder = new Actions(driver);
                //       builder.moveToElement(button).click().perform();
                new WebDriverWait(driver, 1).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#subtab-AdminCatalog[class='maintab  has_submenu hover']")));
//                new WebDriverWait(driver, 1).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#subtab-AdminProducts[data-submenu='10']")));
                break;
            } catch (Exception e) {
 //               eventListener.debug("Ожидание появления подменю!");
            }
            builder.moveToElement(orders).perform();
 //           throw new InterruptedException();
        } while (1 == 1);

        WebElement category = driver.findElement(By.cssSelector("#subtab-AdminProducts[data-submenu='10']"));
        if(browser.equals("ie")) {
            builder.moveToElement(category, 0, -30).click().perform();
        }else {
            builder.moveToElement(category).click().perform();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));

        driver.findElement(By.cssSelector("#page-header-desc-configuration-add")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));

        driver.findElement(By.cssSelector("#form_step1_name_1")).sendKeys(newProduct.getName());
        driver.findElement(By.cssSelector("#form_step1_qty_0_shortcut")).sendKeys(newProduct.getQty().toString());
        driver.findElement(By.cssSelector("#form_step1_price_ttc_shortcut")).sendKeys(newProduct.getPriceInt());

        driver.findElement(By.cssSelector(".switch-input")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.cssSelector(".growl-close")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("growl-message")));

        driver.findElement(By.cssSelector("#submit")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.cssSelector(".growl-close")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("growl-message")));
    }

    public void checkProduct(ProductData newProduct) throws InterruptedException {

        System.out.println(newProduct.getName()+"="+newProduct.getPrice()+"="+newProduct.getQty());
        driver.navigate().to(Properties.getBaseUrl());

        driver.findElement(By.cssSelector(".all-product-link")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("category")));

 //       driver.findElement(By.xpath("//*[contains(.,'"+newProduct.getName()+"')]")).click();
 //       driver.findElement(By.cssSelector(".product-description>.h3.product-title>a:contains('Blouse')")).click();
        List<WebElement> rows =  driver.findElements(By.cssSelector(".product-description>h1>a"));
        Iterator<WebElement> iter = rows.iterator();

        boolean flag = false;
        WebElement item = null;

        while (iter.hasNext()) {
            item = iter.next();
            String label = item.getText().toUpperCase();
            if (label.equals(newProduct.getName().toUpperCase()))
            {
                flag = true;
                break;
            }
        }

        assertTrue (flag, "Элемент не найден");

        item.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));

        assertEquals(driver.findElement(By.cssSelector(".row>div>h1")).getText().toUpperCase(), newProduct.getName().toUpperCase(), "Название продукта не соответствует");
        assertTrue(driver.findElement(By.cssSelector(".current-price>span")).getText().contains(newProduct.getPriceInt()), "Не соответствует цена продукта");
        System.out.println();

 //       driver.findElement(By.linkText("")).click();

//        assertThat("Text not found on page", pageText, containsString(searchText));
    }
        /**
          * Waits until page loader disappears from the page
          */
    public void waitForContentLoad() {
        // TODO implement generic method to wait until page content is loaded

        // wait.until(...);
        // ...
    }
}
