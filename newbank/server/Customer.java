package newbank.server;

import java.util.ArrayList;

public class Customer {

	private ArrayList<Account> accounts;
	private String password;

	public Customer() {
		accounts = new ArrayList<>();
	}

	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString() + ", ";
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}

	public Account getAccountFromName(String accountName){
		for (Account account : this.accounts) {
			if (account.getAccountName().equals(accountName)) {
				return account;
			}
		}
		return null;
	}

	public ArrayList<Account> listAccounts() {
		return accounts;
	}
}
