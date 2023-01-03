package main;
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
            Customer customer = new Customer(password, username);
            customer.authenticate();
            System.out.println("Saldo saat ini: " + customer.getBalance());
        }

    }
}