import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;

public class TestCasesCustomer extends BaseTest {
    String depositAmount;
    String withdrawalAmount;

    // Test case for customer login
    @Test(priority = 1)
    @Parameters("userVisibleText")
    public void customerLogin(String userVisibleText) {
        WebElement customerLoginButton = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/div[1]/button"));
        customerLoginButton.click();

        WebElement users = waitForElementToBeClickable(By.id("userSelect"));
        Select usersSelect = new Select(users);
        usersSelect.selectByVisibleText(userVisibleText);

        WebElement loginButton = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/form/button"));
        loginButton.click();

        WebElement logoutButton = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[1]/button[2]"));
        Assert.assertTrue(logoutButton.isDisplayed(), "Logout button should be displayed.");
    }

    // Test case for deposit
    @Test(priority = 2)
    @Parameters("depositAmount")
    public void deposit(String depositAmount) {
        WebElement depositBtn = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[2]"));
        depositBtn.click();

        WebElement balanceStr = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore = Integer.valueOf(balanceStr.getText());

        WebElement depositInput = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        depositInput.sendKeys(depositAmount);

        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button")).click();
        String depositSuccessMsg = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();

        Assert.assertEquals(depositSuccessMsg, "Deposit Successful", "Deposit should be successful.");
        Integer balanceAfter = Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceAfter - balanceBefore, Integer.valueOf(depositAmount), "Balance should be updated correctly after deposit.");

        this.depositAmount = depositAmount;
    }

    // Test case for withdrawal
    @Test(priority = 3)
    @Parameters("withdrawalAmount")
    public void withdrawal(String withdrawalAmount) {
        WebElement withdrawalBtn = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[3]"));
        withdrawalBtn.click();

        WebElement balanceStr = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore = Integer.valueOf(balanceStr.getText());

        WebElement withdrawalInput = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        withdrawalInput.clear();
        withdrawalInput.sendKeys(withdrawalAmount);

        WebElement actionBtn = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button"));
        actionBtn.click();

        String withdrawalSuccessMsg = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();
        Assert.assertEquals(withdrawalSuccessMsg, "Transaction successful", "Transaction should be successful.");

        Integer balanceAfter = Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceBefore - balanceAfter, Integer.valueOf(withdrawalAmount), "Balance should be updated correctly after withdrawal.");

        this.withdrawalAmount = withdrawalAmount;
    }

    // Test case for withdrawing more than the available balance
    @Test(priority = 4)
    @Parameters("withdrawalBiggerAmount")
    public void withdrawalBiggerAmount(String withdrawalBiggerAmount) {
        WebElement withdrawalBtn = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[3]"));
        withdrawalBtn.click();

        WebElement balanceStr = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore = Integer.valueOf(balanceStr.getText());

        WebElement withdrawalInput = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        withdrawalInput.sendKeys(withdrawalBiggerAmount);

        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button")).click();
        String withdrawalSuccessMsg = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();

        Assert.assertEquals(withdrawalSuccessMsg, "Transaction Failed. You can not withdraw amount more than the balance.", "Transaction should fail.");
        Integer balanceAfter = Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceAfter, balanceBefore, "Balance should remain unchanged.");
    }

    // Test case for viewing transactions
    @Test(priority = 5)
    public void transactions() {
        WebElement transactionsBtn = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[1]"));
        transactionsBtn.click();

        WebElement table = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/table"));
        Assert.assertTrue(table.isDisplayed(), "Transactions table should be displayed.");

        List<WebElement> rows = table.findElements(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/table/tbody/tr"));
        Assert.assertTrue(rows.size() > 0, "Transactions table should have rows.");

        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor0\"]/td[2]")).getText(), depositAmount, "Deposit amount mismatch.");
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor0\"]/td[3]")).getText(), "Credit", "Transaction type mismatch for deposit.");

        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor1\"]/td[2]")).getText(), withdrawalAmount, "Withdrawal amount mismatch.");
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor1\"]/td[3]")).getText(), "Debit", "Transaction type mismatch for withdrawal.");
    }

    // Test case for resetting transactions
    @Test(priority = 6)
    public void transactionsReset() {
        WebElement table = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/table"));
        WebElement resetBtn = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[2]"));
        resetBtn.click();

        boolean isTableEmpty = table.findElements(By.tagName("tr")).size() <= 1;
        Assert.assertTrue(isTableEmpty, "Table should be empty after reset.");

        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[1]")).click();
        WebElement balance = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Assert.assertEquals(balance.getText(), "0", "Balance should be reset to 0.");
    }
}
