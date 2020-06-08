package banking;

import com.sun.jdi.connect.Connector;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;


public class Main {
    Scanner scanner = new Scanner(System.in);
    long id;
    long card;
    int pin;
    long balance;

    Long[] nextLong = {11111l}; // optionally mark as final
    LongSupplier supplier = () -> nextLong[0]++;
    Connecto conn;

    public static void main(String[] args) {
        Main run = new Main();
        //System.out.println(args[0]);
        run.setUp("ok1");
        while (true) {
            run.menu();
        }
    }

    public void setUp(String dtb) {
        conn = new Connecto();
        conn.connect(dtb);
        conn.createCard();
        nextLong[0]= conn.maxNum();
    }

    public void menu() {
        System.out.println("1. Create account" + "\n2. Log into account" + "\n0. Exit");
        int soYouHaveChosen = scanner.nextInt();
        scanner.nextLine();
        switch (soYouHaveChosen) {
            case 2:
                logIntoAccount();
                break;
            case 1:
                newAccount();
                break;
            case 0:
                ende();
        }
    }

    public void newAccount() {
        long cardNum = getNumber();

        int pin =ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
        conn.putCard(cardNum - 4000000000000000l,cardNum, pin, 0);
        System.out.println("\nYour card have been created" + "\nYour card number:" + "\n" + cardNum);
        System.out.println("Your card PIN: \n" + pin + "\n");

    }

    public long getNumber() {

        long x = supplier.getAsLong() * 10;


        long cardNumber = 4000000000000000l + x;
        long sum = 0;
        long n = 0;
        long m = cardNumber;
        ArrayList<Long> numbers = new ArrayList<>();
        while (m > 0) {
            n = m % 10;
            numbers.add(0, n);
            m = m / 10;
        }
        for (Long w : numbers) {
            //System.out.print(w + " ");
        }
        for (int i = 0; i < numbers.size(); i++) {
            if (i % 2 == 0) {
                numbers.add(i, numbers.get(i) * 2);
                numbers.remove(i + 1);
                if (numbers.get(i) > 9) {
                    numbers.add(i, numbers.get(i) - 9);
                    numbers.remove(i + 1);
                }
            }
            sum += numbers.get(i);
        }
        //System.out.println(sum);
        long checksum = (sum * 9) % 10;
        return cardNumber + checksum;
    }

    public void logIntoAccount() {
        System.out.println("Enter your card number:");
        long request = scanner.nextLong();
        scanner.nextLine();
        ResultSet rs =conn.getAccount(request- 4000000000000000l);
        try {

            id=rs.getLong("id");
            card=rs.getLong("number");
            pin=rs.getInt("pin");
            balance=rs.getLong("balance");
           // System.out.println(id+" "+card+" "+pin+" "+balance);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("bad bad"+e);
        }

        System.out.println("Enter your PIN:");
        int passreq = scanner.nextInt();
        scanner.nextLine();
        evaluate(request, passreq);
    }

    public void evaluate(long request, int passreq) {

        if ( passreq == pin && request==card) {

            System.out.println("\nYou have successfully logged in!\n");
            loggedIn();
        } else {
            System.out.println(id+" "+card+" "+pin+" "+balance);
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    public void loggedIn() {
        boolean logged=true;
        while (logged) {
            System.out.println("1. Balance" + "\n2. Add income\3.Close account\n 4.Log out\n5.exit" + "\n0. Exit");
            int soYouHaveChosen = scanner.nextInt();
            scanner.nextLine();
            switch (soYouHaveChosen) {
                case 1:
                    System.out.println("\nBalance: " + balance + "\n");
                    getBalance();
                    break;
                case 2:
                    addIncome();
                    break;
                case 9:
                    doTransfer();
                    break;
                case 3:
                    closeAccount();
                    break;
                case 4:
                    logged=false;
                    System.out.println("You have successfully logged out!");
                    break;
                case 0:
                    ende();
            }
        }
    }

    private void closeAccount() {
        conn.execute("DELETE FROM card where id="+id+";");
    }

    private void doTransfer() {

    }

    private void addIncome() {
        long add= scanner.nextLong();
        conn.putCard(id, card,pin,balance+add);
    }

    private void getBalance() {
        System.out.println("Balance: "+balance);
    }

    public void ende() {
        System.out.println("Bye!");
        conn.close();
        System.exit(0);
    }

}



