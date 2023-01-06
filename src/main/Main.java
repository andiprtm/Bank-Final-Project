package main;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManager.getInstance();

        Scanner input = new Scanner(System.in);
        System.out.println("Selamat datang di ATM Bank Java");
        System.out.println("Masukkan username: ");
        String username = input.nextLine();
        System.out.println("Masukkan password: ");
        String password = input.nextLine();
        System.out.println("Masukkan tipe akun: ");
        String role = input.nextLine();

        if (role.equals("customer")) {
            Customer customer = new Customer(username, password);
            customer.authenticate();
            if (Objects.equals(customer.getAccountType(), "Silver")) {
                Silver silver = new Silver(username, password);
                silver.authenticate();
                silver.getCustomerData();
                silver.transferToAnotherBankAccount(2, BigDecimal.valueOf(200_000));
            } else if (Objects.equals(customer.getAccountType(), "Gold")) {
                Gold gold = new Gold(username, password);
                gold.authenticate();
                gold.getCustomerData();
            } else if (Objects.equals(customer.getAccountType(), "Platinum")) {
                Platinum platinum = new Platinum(username, password);
                platinum.authenticate();
                platinum.getCustomerData();
            }
        } else if (role.equals("employee")){
            Employee employee = new Employee(username, password);
            employee.authenticate();
        }

    }
}