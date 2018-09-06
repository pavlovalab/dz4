package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
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
        WebElement SS = driver.findElement(By.id("email"));
        SS.sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public void createProduct(ProductData newProduct) {
        WebElement catalog = driver.findElement(By.cssSelector("#subtab-AdminCatalog[data-submenu='9']"));
        WebElement orders = driver.findElement(By.cssSelector("#subtab-AdminParentOrders[data-submenu='3']"));

        Actions builder = new Actions(driver);
        do {
            builder.moveToElement(catalog).perform();
            try {
                // Ожидание появления подменю
                new WebDriverWait(driver, 1).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#subtab-AdminProducts[data-submenu='10']")));
                break;
            } catch (Exception e) {
 //               eventListener.debug("Ожидание появления подменю!");
            }
            builder.moveToElement(orders).perform();
        } while (1 == 1);

        WebElement category = driver.findElement(By.cssSelector("#subtab-AdminProducts[data-submenu='10']"));
        builder.moveToElement(category).click().perform();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));

        driver.findElement(By.cssSelector("#page-header-desc-configuration-add")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main-div")));

        driver.findElement(By.cssSelector("#form_step1_name_1")).sendKeys(newProduct.getName());
        driver.findElement(By.cssSelector("#form_step1_qty_0_shortcut")).sendKeys(newProduct.getQty().toString());
        driver.findElement(By.cssSelector("#form_step1_price_shortcut")).sendKeys(newProduct.getPrice());

        driver.findElement(By.cssSelector(".switch-input")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.cssSelector(".growl-close")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("growl-message")));

        driver.findElement(By.cssSelector("#submit")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl-message")));
        driver.findElement(By.cssSelector(".growl-close")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("growl-message")));
    }

    public void checkProduct(ProductData newProduct) {

        driver.navigate().to(Properties.getBaseUrl());

        driver.findElement(By.cssSelector(".all-product-link")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("category")));

        driver.findElement(By.xpath("//*[contains(.,'"+newProduct.getName()+"')]")).click();

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
