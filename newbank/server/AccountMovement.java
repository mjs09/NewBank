package newbank.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountMovement {
    private String accountName;
    private String targetAccountName;
    private double amount;
    private LocalDateTime date;

    public AccountMovement(String accountName, String targetAccountName, double amount, LocalDateTime date) {
        this.accountName = accountName;
        this.targetAccountName = targetAccountName;
        this.amount = amount;
        this.date = date;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getTargetAccountName() {
        return this.targetAccountName;
    }

    public double getAmount() {
        return this.amount;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return this.date.format(myFormatObj) + " ** " + this.targetAccountName + " ** " + String.valueOf(this.amount);
    }
}
