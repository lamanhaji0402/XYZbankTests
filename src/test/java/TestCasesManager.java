import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class TestCasesManager extends BaseTest {

    // Test case to verify the Bank Manager login functionality
    @Test(priority = 1)
    public void bankManagerLogin() {
        WebElement bankManagerLoginBtn = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[1]/button[1]"));
        bankManagerLoginBtn.click();
        WebElement loginBtn = waitForElementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/div[2]/button"));
        loginBtn.click();
        WebElement customerBtn = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[1]"));
        Assert.assertTrue(customerBtn.isDisplayed(), "Customer button should be displayed after login.");
    }

    // Test case to verify the customers table row count
    @Test(priority = 2)
    public void customersTable() {
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[3]")).click();
        WebElement table = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int expectedRowCount = 6;
        Assert.assertEquals(rows.size(), expectedRowCount, "The number of rows in the customer table should be 6.");
    }

    // Test case to search customers by name
    @Test(priority = 3)
    @Parameters("searchQuery")
    public void customersSearchByName(String searchQuery) {
        WebElement search = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/form/div/div/input"));
        search.sendKeys(searchQuery);
        WebElement table = waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (int i = 1; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            List<WebElement> columns = row.findElements(By.tagName("td"));
            boolean isSearchQueryPresent = columns.stream()
                    .anyMatch(column -> column.getText().contains(searchQuery));
            Assert.assertTrue(isSearchQueryPresent, "Search query not found in row: " + row.getText());
        }
        search.clear();
    }

    // Test case to search customers by full name (first and last name)
    @Test(priority = 4)
    @Parameters({"nameSearch", "surnameSearch"})
    public void customersSearchByFullname(String nameSearch, String surnameSearch) {
        WebElement search = waitForElementToBeVisible(By.xpath("//input[@placeholder='Search Customer']"));
        search.sendKeys(nameSearch + " " + surnameSearch);
        WebElement table = waitForElementToBeVisible(By.xpath("//table[@class='table table-bordered table-striped']"));
        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
        Assert.assertTrue(rows.size() > 0, "No customer records found!");

        boolean isMatchFound = false;
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            if (columns.size() >= 2) {
                String rowName = columns.get(0).getText().trim();
                String rowSurname = columns.get(1).getText().trim();
                if (rowName.equals(nameSearch) && rowSurname.equals(surnameSearch)) {
                    isMatchFound = true;
                    break;
                }
            }
        }
        Assert.assertTrue(isMatchFound, "Customer with the name '" + nameSearch + " " + surnameSearch + "' not found in the table.");
        search.clear();
    }

    // Test case to add a new customer
    @Test(priority = 5)
    @Parameters({"firstName","lastName","postCode"})
    public void addCustomer(String firstName,String lastName,String postCode){
        WebElement customerBtn=driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[1]"));
        customerBtn.click();
        WebElement firstNameInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/form/div[1]/input"));
        firstNameInput.sendKeys(firstName);
        WebElement lastNameInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/form/div[2]/input"));
        lastNameInput.sendKeys(lastName);
        WebElement postCodeInput=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/form/div[3]/input"));
        postCodeInput.sendKeys(postCode);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/form/button")).click();
        Alert alert=driver.switchTo().alert();
        alert.accept();
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[3]")).click();
        WebElement search=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/form/div/div/input"));
        search.sendKeys(firstName);
        WebElement table=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        boolean isAtLeastOneMatch = false;

        for (int i = 1; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            List<WebElement> columns = row.findElements(By.tagName("td"));
            String rowName = columns.get(0).getText();
            String rowSurname = columns.get(1).getText();
            String rowPostCode=columns.get(2).getText();
            boolean isNameMatch = rowName.contains(firstName);
            boolean isSurnameMatch = rowSurname.contains(lastName);
            boolean isPostCodeMatch=rowPostCode.contains(postCode);
            if (isNameMatch && isSurnameMatch && isPostCodeMatch) {
                isAtLeastOneMatch = true;
                break;
            }
        }
        Assert.assertTrue(isAtLeastOneMatch, "No matching row found in the table");
        search.clear();
    }

    // Test case to open an account for a customer
    @Test(priority = 6)
    @Parameters({"firstName","lastName","currency"})
    public void openAccount(String firstName,String lastname,String currency){
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[2]")).click();
        WebElement customer=waitForElementToBeClickable(By.xpath("//*[@id=\"userSelect\"]"));
        Select selectCust=new Select(customer);
        selectCust.selectByVisibleText(firstName+" "+lastname);
        WebElement currencyEl=driver.findElement(By.xpath("//*[@id=\"currency\"]"));
        Select selectCurr=new Select(currencyEl);
        selectCurr.selectByVisibleText(currency);
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/form/button")).click();
        Alert alert=driver.switchTo().alert();
        alert.accept();
        driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[1]/button[3]")).click();
        WebElement search=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/form/div/div/input"));
        search.sendKeys(firstName);
        WebElement table=waitForElementToBeVisible(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement row = rows.get(1);
        List<WebElement> columns = row.findElements(By.tagName("td"));
        String rowAccount = columns.get(3).getText();
        Assert.assertTrue(rowAccount != null && rowAccount.matches("-?\\d+"));//if it is numerical then true
    }

    // Test case to delete a customer
    @Test(priority = 7)
    public void deleteCustomer(){
        WebElement deleteBtn=driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table/tbody/tr[1]/td[5]/button"));
        deleteBtn.click();
        WebElement table= driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int expectedRowCount = 1;//only header row
        Assert.assertEquals(rows.size(), expectedRowCount);
    }
}
