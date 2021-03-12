package newbank.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String addAccount(CustomerID customer, String accountName, double balance) {
		customers.get(customer.getKey()).addAccount(new Account(accountName, balance));
		return "SUCCESS";
	}

	//Testing git branches and git merge

}
