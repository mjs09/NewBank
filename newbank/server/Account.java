package newbank.server;

public class Account {

	private String accountName;
	private double openingBalance;
	private double currentBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;

		// Set starting currentBalance to openingBalance
		this.currentBalance = this.openingBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getCurrentBalance() {
		return this.currentBalance;
	}

	// Shows the account currentBalance
	public String toString() {
		return (accountName + ": " + currentBalance);
	}

}
