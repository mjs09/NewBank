package newbank.server;
import java.util.*;

public class ExchangeRates {

    private ArrayList<String> currencyType = new ArrayList<>();
    private Double exchangeRate;


    public ExchangeRates() { 
	
	
        ArrayList<String> currencyType = new ArrayList<>();
        currencyType.add("EUR");
        currencyType.add("USD");
        currencyType.add("YUAN");

    }

    public static Double getExchangeRate(String currency) { 
        Double currValue = 0.00;

        HashMap<String, Double> currencyName = new HashMap<String, Double>();

        //add elements to HashMap
        currencyName.put("EUR", 1.25);
        currencyName.put("USD", 0.99);
        currencyName.put("YUAN", 5.25);

           if(currencyName.containsKey(currency)){
                currValue = currencyName.get(currency);
           }
           return currValue;

    }

    public static String getCurrencyType(String currency) { 

        ArrayList<String> currencyType = new ArrayList<>();

        currencyType.add("EUR");
        currencyType.add("USD");
        currencyType.add("YUAN");

        boolean containsFXRate = true;

        if (!currencyType.contains(currency)) {
            containsFXRate = false;

        } else {
            return currency;
        }
        return "Currency not Found";
    }
