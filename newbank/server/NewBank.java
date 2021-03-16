package newbank.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.addAccount(new Account("Savings", 10000.0));
		bhagy.addAccount(new Account("Checking", 150.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Main", 150.0));
		christina.addAccount(new Account("Savings", 1500.0));
		christina.addAccount(new Account("Checking", 250.0));
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Main", 300.0));
		john.addAccount(new Account("Savings", 3000.0));
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, double amount, String source, String target) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
				case "MOVE":
					return moveMoney(customer, amount, source, target);
				default:
					return "FAIL";
			}
		}
		return "FAIL";
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
			case "SHOWMYACCOUNTS":
				return showMyAccounts(customer);
			default:
				return "FAIL";
			}
		}
		return "FAIL";
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, String accountName, double balance) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
			case "NEWACCOUNT":
				return addAccount(customer, accountName, balance);
			default:
				return "FAIL";
			}
		}
		return "FAIL";
	}
	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, double value, String source, String payee) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
				case "PAY":
					return payMoney(customer, value, source, payee);
				default:
					return "FAIL";
			}
		}
		return "SUCCESS";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String addAccount(CustomerID customer, String accountName, double balance) {
		customers.get(customer.getKey()).addAccount(new Account(accountName, balance));
		return "SUCCESS";
	}

	private String moveMoney(CustomerID customer, double amount, String source, String target) {
		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();
		for (Account a : accountsList) {
			if (a.getAccountName().equals(source)) {
				if (a.getCurrentBalance() < amount) {
					return "FAIL: Insufficient funds in source account";
				}
				else {
					double currentBalance = a.getCurrentBalance();
					double newBalance = currentBalance - amount;
					a.setCurrentBalance(newBalance);
					for (Account a2 : accountsList) {
						if (a2.getAccountName().equals(target)) {
							double currentBalance2 = a2.getCurrentBalance();
							double newBalance2 = currentBalance2 + amount;
							a2.setCurrentBalance(newBalance2);
						}
					}
					return "SUCCESS";
				}
			}
		}


		return "FAIL";
	}
	private String payMoney(CustomerID customer, double amount, String source, String payee) {
		String name = payee;
		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();
		for (Account customerAccount : accountsList) {
			if (customerAccount.getAccountName().equals(source)) {
				if (customerAccount.getCurrentBalance() < amount) {
					return "FAIL: Insufficient funds in source account";
				}
				else {
					double currentBalance = customerAccount.getCurrentBalance();
					double newBalance = currentBalance - amount;
					customerAccount.setCurrentBalance(newBalance);
					for (Account paymentAccount : accountsList) {
						if (payee == name) {
							double currentBalance2 = paymentAccount.getCurrentBalance();
							double newBalance2 = currentBalance2 + amount;
							paymentAccount.setCurrentBalance(newBalance2);
						}
					}
					return "SUCCESS";
				}
			}
		}


		return "FAIL";


}
