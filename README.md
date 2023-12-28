 XYZ Bank Tests

 Overview

This project contains automated tests for the XYZ Bank web application. The tests are designed to cover various functionalities for both bank managers and customers using Selenium WebDriver and TestNG.

 Project Structure

- BaseTest.java: This class contains common setup and teardown methods for the WebDriver, such as launching and terminating the browser. It also includes helper methods for waiting for elements to be visible or clickable.

- TestCasesManager.java: This class includes test cases related to bank manager functionalities. Methods cover bank manager login, customer table verification, customer searches, adding customers, opening accounts, and deleting customers.

- TestCasesCustomer.java: This class focuses on customer-related test cases, including customer login, deposit, withdrawal, transaction verification, and resetting transactions.

 Test Execution

To run the tests, execute the provided TestNG XML file (`testng.xml`). This file orchestrates the execution of both manager and customer test classes.

Manager Tests

- Bank Manager Login: Verifies the successful login of a bank manager.
- Customer Table: Checks the presence and count of customers in the table.
- Customer Search: Searches for customers by name and validates the results.
- Customer Search by Full Name: Searches for customers by full name and validates the results.
- Add Customer: Adds a new customer and verifies its presence in the customer table.
- Open Account: Opens an account for a customer and verifies the account details.
- Delete Customer: Deletes a customer and ensures removal from the customer table.

 Customer Tests

- Customer Login: Logs in as a customer and verifies successful login.
- Deposit: Performs a deposit transaction and validates the transaction success and balance update.
- Withdrawal: Performs a withdrawal transaction and validates the transaction success and balance update.
- Withdrawal with Insufficient Funds: Attempts to withdraw an amount greater than the balance and verifies the failure.
- Transactions: Checks the transaction history and verifies the recorded transactions.
- Reset Transactions: Resets the transaction history and ensures the table is empty.

 Load Testing

For load testing JMeter was used to simulate multiple users accessing the application simultaneously in different scenarios.

Jira: https://laman-haji.atlassian.net/jira/software/projects/XB/boards/6

