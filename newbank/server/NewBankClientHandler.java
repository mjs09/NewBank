package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

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
			while (true) {
				// ask for user name
				out.println("Enter Username");
				String userName = in.readLine();
				// ask for password
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				// authenticate user and get customer ID token from bank for use in subsequent
				// requests
				CustomerID customer = bank.checkLogInDetails(userName, password);

				if (customer != null) {
					// if the user is authenticated then get requests from the user and process them
					out.println("Log In Successful. What do you want to do?");

					while (true) {
						String request = in.readLine();
						System.out.println("Request from " + customer.getKey());

						if (request.equals("LOGOUT")){
							out.println("Logging out...");
							break;
						}

						else if (request.equals("NEWACCOUNT")) {
							out.println("Account Name:");
							String accountName = in.readLine();
							out.println("Opening Balance");
							double balance = Double.parseDouble(in.readLine());
							String responce = bank.processRequest(customer, request, accountName, balance);
							out.println(responce);

						} else if (request.equals("MOVE")) {
							out.println("Amount to move:");
							// double amount = scan.nextDouble();
							String amountString = in.readLine();
							double amount = Double.parseDouble(amountString);
							out.println("From account:");
							String source = in.readLine();
							out.println("To account");
							String target = in.readLine();
							String responce = bank.processRequest(customer, request, source, target, amount);
							out.println(responce);

						} else if (request.equals("CLOSEACCOUNT")) {
							out.println("Account to close:");
							String account = in.readLine();
							String responce = bank.processRequest(customer, request, account);
							out.println(responce);

						}

						else if (request.equals("PAY")) {
							out.println("Amount to pay: ");
							String moneyString = in.readLine();
							double value = Double.parseDouble(moneyString);

							out.println("From account: ");
							String source = in.readLine();

							out.println("To payee: ");
							CustomerID payee = new CustomerID(in.readLine());

							out.println("To payee account: ");
							String payeeAccount = in.readLine();

							String responce = bank.processRequest(customer, request, value, source, payee, payeeAccount);
							out.println(responce);
						}

						else if (request.equals("SHOWACCOUNTMOVEMENTS")) {

							out.println("From account: ");
							String account = in.readLine();

							String movements = bank.processRequest(customer, request, account);

							String[] array = movements.split(";");

							for (String string : array) {
								out.println(string);
							}

						}

						else if (request.equals("SETOVERDRAFT")){
							out.println("Which account do you want to set the overdraft for?");
							String account = in.readLine();

							out.println("What overdraft amount would you like?");
							double amount = Double.parseDouble(in.readLine());

							String responce = bank.processRequest(customer, request, account, amount);
							out.println(responce);
						}

						else {
							String responce = bank.processRequest(customer, request);
							out.println(responce);
						}
					}
				} else {
					out.println("Log In Failed");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
