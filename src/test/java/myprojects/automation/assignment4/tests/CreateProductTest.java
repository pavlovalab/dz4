package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @DataProvider(name = "Autorization")
    public static Object[][] getAccount() {
        return new String[][] { { "webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw" } };
    }
    @Test(dataProvider = "Autorization")
    public void createNewProduct(String login, String password) throws InterruptedException {
        // TODO implement test for product creation

        actions.login(login, password);

        ProductData product = ProductData.generate();
        actions.createProduct(product);

        actions.checkProduct(product);
        Thread.sleep(10000);
        // ...
    }

    // TODO implement logic to check product visibility on website
}
