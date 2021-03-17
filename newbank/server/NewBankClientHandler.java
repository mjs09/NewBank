package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			// ask for user name
			out.println("Enter Username");
			String userName = in.readLine();
			// ask for password
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			CustomerID customer = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them 
			if(customer != null) {
				out.println("Log In Successful. What do you want to do?");
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					if (request.equals("NEWACCOUNT")){
						out.println("Account Name:");
						String accountName = in.readLine();
						out.println("Opening Balance");
						double balance = Double.parseDouble(in.readLine());
						String responce = bank.processRequest(customer, request, accountName, balance);
						out.println(responce);
					}
					else if (request.equals("MOVE")) {
						Scanner scan = new Scanner(System.in);
						out.println("Amount to move:");
						//double amount = scan.nextDouble();
						String amountString = in.readLine();
						double amount = Double.parseDouble(amountString);
						out.println("From account:");
						String source = in.readLine();
						out.println("To account");
						String target = in.readLine();
						String responce = bank.processRequest(customer, request, source, target, amount);
						out.println(responce);
						
					}
					else if (request.equals("CLOSEACCOUNT")) {
						Scanner scan = new Scanner(System.in);
						out.println("Account to close:");
						String account = in.readLine();
						String responce = bank.processRequest(customer, request, account);
						out.println(responce);

					}
					
					else if (request.equals("PAY")) {
						Scanner input = new Scanner(System.in);
						out.println("Amount to pay: ");
						String moneyString = in.readLine();
						double value = Double.parseDouble(moneyString);

						out.println("From account: ");
						String source = in.readLine();

						out.println("To Who: ");
						String payee = in.readLine();

						String responce = bank.processRequest(customer, request, value, source, payee);
						out.println(responce);
					}
					
					else{
						String responce = bank.processRequest(customer, request);
						out.println(responce);
					}
				}
			}
			else {
				out.println("Log In Failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
