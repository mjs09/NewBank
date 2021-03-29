package newbank.server;

import java.time.LocalDateTime;
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
		bhagy.setPassword("1234");
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.addAccount(new Account("Savings", 10000.0));
		bhagy.addAccount(new Account("Checking", 150.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.setPassword("0000");
		christina.addAccount(new Account("Main", 150.0));
		christina.addAccount(new Account("Savings", 1500.0));
		christina.addAccount(new Account("Checking", 250.0));
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.setPassword("9999");
		john.addAccount(new Account("Main", 300.0));
		john.addAccount(new Account("Savings", 3000.0));
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName) && customers.get(userName).getPassword().equals(password)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, String source, String target,
			double amount) {
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
	public synchronized String processRequest(CustomerID customer, String request, String account) {
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
			case "CLOSEACCOUNT":
				return closeAccount(customer, account);
			case "SHOWACCOUNTMOVEMENTS":
				return getAccountMovements(customer, account);
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
			case "SETOVERDRAFT":
				return setOverdraft(customer, accountName, balance);
			default:
				return "FAIL";
			}
		}
		return "FAIL";
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request, double value, String source,
			String payee) {
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
				} else {
					double currentBalance = a.getCurrentBalance();
					double newBalance = currentBalance - amount;
					a.setCurrentBalance(newBalance);
					a.addMovement(new AccountMovement(source, target, -amount, LocalDateTime.now()));
					for (Account a2 : accountsList) {
						if (a2.getAccountName().equals(target)) {
							double currentBalance2 = a2.getCurrentBalance();
							double newBalance2 = currentBalance2 + amount;
							a2.setCurrentBalance(newBalance2);
							a2.addMovement(new AccountMovement(source, target, amount, LocalDateTime.now()));
						}
					}
					return "SUCCESS";
				}
			}
		}
		return "FAIL";
	}

	private String closeAccount(CustomerID customer, String account) {
		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();
		int index = 0;
		for (Account a : accountsList) {
			if (a.getAccountName().equals(account)) {
				if (a.getCurrentBalance() != 0) {
					return "FAIL: Account has a non-zero balance. Please use MOVE or PAY command to move or pay money into other accounts.";
				} else {
					accountsList.remove(index);
					return "SUCCESS";
				}
			}
			index++;
		}
		return "FAIL: Account not found. Use SHOWMYACCOUNTS to check account name. Account names are case sensitive.";
	}

	private String payMoney(CustomerID customer, double amount, String source, String payee) {
		String name = payee;
		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();
		for (Account customerAccount : accountsList) {
			if (customerAccount.getAccountName().equals(source)) {
				if (customerAccount.getCurrentBalance() < amount) {
					return "FAIL: Insufficient funds in source account";
				} else {
					double currentBalance = customerAccount.getCurrentBalance();
					double newBalance = currentBalance;
					customerAccount.setCurrentBalance(newBalance);
					customerAccount.addMovement(new AccountMovement(source, payee, amount, LocalDateTime.now()));
					for (Account paymentAccount : accountsList) {
						if (payee == name) {
							double currentBalance2 = paymentAccount.getCurrentBalance();
							double newBalance2 = currentBalance2 - amount;
							paymentAccount.setCurrentBalance(newBalance2);
							paymentAccount.addMovement(new AccountMovement(source, payee, amount, LocalDateTime.now()));
						}
					}
					return "SUCCESS";
				}
			}
		}

		return "FAIL";

	}

	private String getAccountMovements(CustomerID customer, String accountName) {

		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();

		String accountMov = "";

		for (Account account : accountsList) {
			if (account.getAccountName().equals(accountName)) {
				for (AccountMovement movement : account.getMovements()) {
					accountMov += movement.toString() + ";";
				}
			}
		}

		return accountMov;
	}


	private String setOverdraft(CustomerID customer, String accountName, Double amount){
		customers.get(customer.getKey()).getAccountFromName(accountName).setOverdraft(amount);
		return "SUCCESS";

	}
  
  	private String intlPayment(CustomerID customer, double value, String source, String payee, String currency) {
		String name = payee;
		ArrayList<Account> accountsList = customers.get(customer.getKey()).listAccounts();
		for (Account customerAccount : accountsList) {
			if (customerAccount.getAccountName().equals(source)) {
				if (customerAccount.getCurrentBalance() < value) {
					return "FAIL: Insufficient funds in source account";
				} else {
					double currentBalance = customerAccount.getCurrentBalance();
					double newBalance = currentBalance;
					customerAccount.setCurrentBalance(newBalance);
					customerAccount.addMovement(new AccountMovement(source, payee, amount, LocalDateTime.now()));
					for (Account paymentAccount : accountsList) {
						if (paymentAccount.getAccountName().equals(source)) {
							if (currency == getCurrencyType(currency)) {
								double currentBalance2 = paymentAccount.getCurrentBalance();
								double newBalance2 = currentBalance2 - value;
								double currencyEquivalent = currentBalance2 * getExchangeRate(currency);
								paymentAccount.setCurrentBalance(newBalance2);
								paymentAccount.addMovement(new AccountMovement(source, payee, amount, LocalDateTime.now()));
							} 
						}
						return getCurrencyType(currency) + "\\GBP " + getExchangeRate(currency) +  " SUCCESS" ;
					}
				}
			}

		}
		return "FAIL";
  
}

