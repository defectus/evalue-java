package tk.deftech.evalue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private final static int ONE_MINUTE = 60 * 1000, NO_DELAY = 0;
    private ConcurrentHashMap<String, BigDecimal> values = new ConcurrentHashMap<>();

    private static class PaymentEntry {

        private String currencyCode;
        private BigDecimal amount;

        public PaymentEntry(String currencyCode, BigDecimal amount) {
            this.currencyCode = currencyCode;
            this.amount = amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public String generateOutput(Map<String, BigDecimal> values) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, BigDecimal> value : values.entrySet()) {
            builder.append(value.getKey()).append(" ").append(value.getValue()).append('\n');
        }
        return builder.toString();
    }

    public void initPrintPaymentsEachMinute() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String output = generateOutput(values);
                System.out.println(output);
            }
        }, NO_DELAY, ONE_MINUTE);
    }

    public void addPayment(PaymentEntry paymentEntry) {
        if (values.containsKey(paymentEntry.getCurrencyCode())) {
            BigDecimal newValue = values.get(paymentEntry.getCurrencyCode()).add(paymentEntry.getAmount());
            if (newValue.equals(BigDecimal.ZERO)) {
                values.remove(paymentEntry.getCurrencyCode());
            } else {
                values.put(paymentEntry.getCurrencyCode(),
                    values.get(paymentEntry.getCurrencyCode()).add(paymentEntry.getAmount()));
            }
        } else {
            values.put(paymentEntry.getCurrencyCode(), paymentEntry.getAmount());
        }
    }

    public PaymentEntry parseLine(String line) {
        assert line != null : "Entry string cannot be null";
        assert !line.isEmpty() : "Entry string cannot be empty";
        String[] parsedLine = line.split(" ");
        assert parsedLine.length == 2 : "Entry string format incorrect";
        return new PaymentEntry(parsedLine[0], new BigDecimal(parsedLine[1]));
    }

    public void parseFile(String fileName) {
        try (BufferedReader fileToRead = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = fileToRead.readLine()) != null) {
                PaymentEntry paymentEntry = parseLine(line);
                addPayment(paymentEntry);
            }
        }
        catch (IOException e) {
            System.err.println("Error reading source file " + fileName);
        }
    }

    public void mainLoop() {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                if ("quit".equalsIgnoreCase(line)) {
                    break;
                }
                PaymentEntry paymentEntry = parseLine(line);
                addPayment(paymentEntry);
            }
        }
        catch (IOException e) {
            System.err.println("Error reading input");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.initPrintPaymentsEachMinute();
        if (args.length > 0) {
            main.parseFile(args[0]);
        }
        main.mainLoop();
    }
}
