import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestCasesCustomer extends BaseTest{
    String depositAmount;
    String withdrawalAmont;
    @Test(priority = 1)
    @Parameters("userVisibleText")
    public void customerLogin(String userVisibleText){
        WebElement customerLoginButton = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/div[1]/button"));
        customerLoginButton.click();
        WebElement users=waitForElementToBeClickable(By.id("userSelect"));
        Select usersSelect=new Select(users);
        usersSelect.selectByVisibleText(userVisibleText);
        WebElement loginButton=waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/form/button"));
        loginButton.click();
        WebElement logoutButton=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[1]/button[2]"));
        Assert.assertTrue(logoutButton.isDisplayed());
    }
    @Test(priority = 2)
    @Parameters("depositAmount")
    public void deposit(String depositAmount){
        WebElement depositBtn=waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[2]"));
        depositBtn.click();
        WebElement balanceStr= waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore= Integer.valueOf(balanceStr.getText());
        WebElement depositInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        depositInput.sendKeys(depositAmount);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button")).click();
        String depositSuccessMsg=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();
        Assert.assertEquals(depositSuccessMsg,"Deposit Successful");
        Integer balanceAfter=Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceAfter-balanceBefore,Integer.valueOf(depositAmount));
        this.depositAmount=depositAmount;
    }
    @Test(priority = 3)
    @Parameters("withdrawalAmount")
    public void withdrawal(String withdrawalAmount) throws TimeoutException{
        WebElement withdrawalBtn=waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[3]"));
        withdrawalBtn.click();
        WebElement balanceStr= waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore= Integer.valueOf(balanceStr.getText());
        WebElement withdrawalInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        withdrawalInput.sendKeys(withdrawalAmount);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button")).click();
        String withdrawalSuccessMsg=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();
        Assert.assertEquals(withdrawalSuccessMsg,"Transaction successful");
        Integer balanceAfter=Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceBefore-balanceAfter,Integer.valueOf(withdrawalAmount));
        this.withdrawalAmont=withdrawalAmount;
    }
    @Test(priority = 4)
    @Parameters("withdrawalBiggerAmount")
    public void withdrawalBiggerAmount(String withdrawalBiggerAmount){
        WebElement withdrawalBtn=waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[3]"));
        withdrawalBtn.click();
        WebElement balanceStr= waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Integer balanceBefore= Integer.valueOf(balanceStr.getText());
        WebElement withdrawalInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/div/input"));
        withdrawalInput.sendKeys(withdrawalBiggerAmount);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/form/button")).click();
        String withdrawalSuccessMsg=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[4]/div/span")).getText();
        Assert.assertEquals(withdrawalSuccessMsg,"Transaction Failed. You can not withdraw amount more than the balance.");
        Integer balanceAfter=Integer.valueOf(balanceStr.getText());
        Assert.assertEquals(balanceAfter,balanceBefore);
    }
    @Test(priority = 5)
    public void transactions() throws TimeoutException{
        WebElement transactionsBtn=waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[3]/button[1]"));
        transactionsBtn.click();
        WebElement table=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/table"));
        Assert.assertTrue(table.isDisplayed());
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor0\"]/td[2]")).getText(),depositAmount);
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor0\"]/td[3]")).getText(),"Credit");
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor1\"]/td[2]")).getText(),withdrawalAmont);
        Assert.assertEquals(waitForElementToBeVisible(By.xpath("//*[@id=\"anchor1\"]/td[3]")).getText(),"Debit");
    }
    @Test(priority = 6)
    public void transactionsReset(){
        WebElement table=driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/table"));
        WebElement resetBtn=driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[2]"));
        resetBtn.click();
        boolean isTableEmpty = table.findElements(By.tagName("tr")).size() <= 1; // Assuming the table rows (tr) contain data
        Assert.assertTrue(isTableEmpty);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[1]")).click();
        WebElement balance=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/strong[2]"));
        Assert.assertEquals(balance.getText(),"0");
    }

}
