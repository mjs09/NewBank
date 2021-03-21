package newbank.server;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private String accountName;
	private double openingBalance;
	private double currentBalance;

	private List<AccountMovement> movements;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;

		// Set starting currentBalance to openingBalance
		this.currentBalance = this.openingBalance;

		// Instantiate list
		this.movements = new ArrayList<AccountMovement>();
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getCurrentBalance() {
		return this.currentBalance;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public List<AccountMovement> getMovements() {
		return this.movements;
	}

	public void addMovement(AccountMovement m) {
		this.movements.add(m);
	}

	// Shows the account currentBalance
	public String toString() {
		return (accountName + ": " + currentBalance);
	}

}
