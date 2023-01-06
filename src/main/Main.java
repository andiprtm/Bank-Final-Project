package main;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManager.getInstance();
        System.out.println();

        Scanner input = new Scanner(System.in);
        System.out.println("Selamat datang di ATM Bank Java");
        System.out.println("Masukkan username: ");
        String username = input.nextLine();
        System.out.println("Masukkan password: ");
        String password = input.nextLine();
        System.out.println("Masukkan role akun: ");
        String role = input.nextLine();

        if (role.equals("customer")) {
            customerMenu(username, password);
        } else if (role.equals("employee")){
            Employee employee = new Employee(username, password);
            employee.authenticate();
        }

    }

    public static Integer chooseCustomerMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih menu di bawah ini");
        System.out.println("1. Transfer");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");

        System.out.println();

        System.out.println("Pilih menu: ");

        return Integer.parseInt(input.nextLine());
    }

    public static void customerMenu(String username, String password) {
        Scanner input = new Scanner(System.in);

        Customer customer = new Customer(username, password);
        customer.authenticate();

        System.out.println();

        if (customer.isActive) {
            if (Objects.equals(customer.getAccountType(), "Silver")) {
                Silver silver = new Silver(username, password);
                silver.authenticate();
                silver.getCustomerData();

                System.out.println();
                int menu = chooseCustomerMenu();
                System.out.println();

                if (menu == 1) {
                    System.out.println("========== Transfer saldo ke rekening lain ==========");
                    System.out.println("Masukkan username rekening: ");
                    String usernameReceiver = input.nextLine();
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    silver.transferToAnotherBankAccount(usernameReceiver, amount);
                } else if (menu == 2) {
                    System.out.println("========== Withdraw/Penarikan saldo rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    silver.withdrawBalance(amount);
                } else if (menu == 3) {
                    System.out.println("========== Deposit saldo ke rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    silver.depositBalance(amount);
                } else {
                    System.out.println("Menu tidak di temukan!");
                }
            } else if (Objects.equals(customer.getAccountType(), "Gold")) {
                Gold gold = new Gold(username, password);
                gold.authenticate();
                gold.getCustomerData();

                System.out.println();
                int menu = chooseCustomerMenu();
                System.out.println();

                if (menu == 1) {
                    System.out.println("========== Transfer saldo ke rekening lain ==========");
                    System.out.println("Masukkan username rekening: ");
                    String usernameReceiver = input.nextLine();
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    gold.transferToAnotherBankAccount(usernameReceiver, amount);
                } else if (menu == 2) {
                    System.out.println("========== Withdraw/Penarikan saldo rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    gold.withdrawBalance(amount);
                } else if (menu == 3) {
                    System.out.println("========== Deposit saldo ke rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    gold.depositBalance(amount);
                } else {
                    System.out.println("Menu tidak di temukan!");
                }
            } else if (Objects.equals(customer.getAccountType(), "Platinum")) {
                Platinum platinum = new Platinum(username, password);
                platinum.authenticate();
                platinum.getCustomerData();

                System.out.println();
                int menu = chooseCustomerMenu();
                System.out.println();

                if (menu == 1) {
                    System.out.println("========== Transfer saldo ke rekening lain ==========");
                    System.out.println("Masukkan username rekening: ");
                    String usernameReceiver = input.nextLine();
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    platinum.transferToAnotherBankAccount(usernameReceiver, amount);
                } else if (menu == 2) {
                    System.out.println("========== Withdraw/Penarikan saldo rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    platinum.withdrawBalance(amount);
                } else if (menu == 3) {
                    System.out.println("========== Deposit saldo ke rekening ==========");
                    System.out.println("Masukkan nominal: ");
                    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(input.nextLine()));

                    System.out.println();

                    platinum.depositBalance(amount);
                } else {
                    System.out.println("Menu tidak di temukan!");
                }
            }
        } else {
            System.out.println("Akun anda tidak aktif atau di blokir!");
        }
    }
}