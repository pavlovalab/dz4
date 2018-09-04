package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.utils.Properties;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @Test
    @Parameters({"selenium.login", "selenium.password"})
    public void createNewProduct(String login, String password) {
        // TODO implement test for product creation

        driver.get(Properties.getBaseUrl());
        System.out.println("Supper!!");
        // actions.login(login, password);
        // ...
    }

    // TODO implement logic to check product visibility on website
}
