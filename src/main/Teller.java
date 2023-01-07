package main;

import java.math.BigDecimal;
import java.util.Objects;

public class Teller extends Employee {
    public Teller(String username, String password) {
        super(username, password);
    }

    public void createCustomerAccount (String accountType, String username, String password, String name, String address, String phone, BigDecimal accountBalance, Integer pin) {
        if (Objects.equals(accountType, "Silver")) {
            Silver silver = new Silver(accountType, username, password, name, address, phone, accountBalance, pin);
            silver.authenticate();
            silver.getCustomerData();
            silver.displayCustomerData();
        } else if (Objects.equals(accountType, "Gold")) {
            Gold gold = new Gold(accountType, username, password, name, address, phone, accountBalance, pin);
            gold.authenticate();
            gold.getCustomerData();
            gold.displayCustomerData();
        } else if (Objects.equals(accountType, "Platinum")) {
            Platinum platinum = new Platinum(accountType, username, password, name, address, phone, accountBalance, pin);
            platinum.authenticate();
            platinum.getCustomerData();
            platinum.displayCustomerData();
        }
    }

    public void setIsActiveCustomerAccount (String username, Boolean isActive) {
        Customer customer = new Customer(username);
        customer.setIsActive(isActive);

        if (customer.isActive == null) {
            System.out.println("Gagal mengubah status rekening nasabah!");
        } else if (customer.isActive) {
            System.out.println("Berhasil buka rekening nasabah " + username + "!");
        } else {
            System.out.println("Berhasil tutup rekening nasabah " + username + "!");
        }
    }

    public void depositBalanceToCustomerAccount (String username, BigDecimal amount) {
        Customer customer = new Customer(username);
        customer.getCustomerData();

        if (Objects.equals(accountType, "Silver")) {
            Silver silver = new Silver(username);
            silver.getCustomerData();
            silver.depositBalance(amount);
        } else if (Objects.equals(accountType, "Gold")) {
            Gold gold = new Gold(username);
            gold.getCustomerData();
            gold.displayCustomerData();
            gold.depositBalance(amount);
        } else if (Objects.equals(accountType, "Platinum")) {
            Platinum platinum = new Platinum(username);
            platinum.getCustomerData();
            platinum.displayCustomerData();
            platinum.depositBalance(amount);
        }
    }

    public void updateDataCustomerAccount (String username, String accountType, String newUsername, String password, String name, String address, String phone, Integer pin) {
        Customer customer = new Customer(username);
        customer.getCustomerData();

        customer.updateCustomerData(name, address, phone);
        customer.updateCustomerBankAccount(accountType, pin);
        customer.updateCustomerLogin(newUsername, password);

        System.out.println("Berhasil mengupdate data nasabah!");
    }
}
