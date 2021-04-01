package newbank.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountMovement {
    private String description;
    private double amount;
    private LocalDateTime date;

    public AccountMovement(String description, double amount, LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getTargetAccountName() {
        return this.description;
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
        return this.date.format(myFormatObj) + " ** " + this.description + " ** " + String.valueOf(this.amount);
    }
}
