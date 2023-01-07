package main;
import java.math.BigDecimal;
import java.sql.SQLException;
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
            employeeMenu(username, password);
        }

    }

    public static Integer chooseTellerMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih menu di bawah ini");
        System.out.println("1. Tambah Nasabah");
        System.out.println("2. Tutup Rekening Nasabah");
        System.out.println("3. Buka Rekening Nasabah");
        System.out.println("4. Deposit Uang Nasabah");
        System.out.println("5. Edit Data Nasabah");
        System.out.println("6. Data Transaksi Nasabah");

        System.out.println();

        System.out.println("Pilih menu: ");

        return Integer.parseInt(input.nextLine());
    }

    public static Integer chooseManagerMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih menu di bawah ini");
        System.out.println("1. Tambah Pegawai");
        System.out.println("2. Hapus Pegawai");
        System.out.println("3. Edit Data Pegawai");

        System.out.println();

        System.out.println("Pilih menu: ");

        return Integer.parseInt(input.nextLine());
    }

    public static void employeeMenu(String username, String password) {
        Scanner input = new Scanner(System.in);

        Employee employee = new Employee(username, password);
        employee.authenticate();

        System.out.println();

        if (employee.isActive) {
            if (Objects.equals(employee.accountType, "Teller")) {
                Teller teller = new Teller(username, password);
                teller.authenticate();
                teller.getEmployeeData();

                System.out.println();
                int menu = chooseTellerMenu();
                System.out.println();

                if (menu == 1) {
                    System.out.println("========== Tambah nasabah baru ==========");
                    System.out.println("Masukkan tipe akun nasabah: ");
                    String accountTypeCustomer = input.nextLine();
                    System.out.println("Masukkan username nasabah: ");
                    String usernameCustomer = input.nextLine();
                    System.out.println("Masukkan password nasabah: ");
                    String passwordCustomer = input.nextLine();
                    System.out.println("Masukkan nama lengkap nasabah: ");
                    String nameCustomer = input.nextLine();
                    System.out.println("Masukkan alamat lengkap nasabah: ");
                    String addressCustomer = input.nextLine();
                    System.out.println("Masukan nomor telefon nasabah: ");
                    String phoneCustomer = input.nextLine();
                    System.out.println("Masukkan deposit awal nasabah: ");
                    BigDecimal accountBalanceCustomer = BigDecimal.valueOf(input.nextLong());
                    System.out.println("Masukkan pin nasabah: ");
                    Integer pinCustomer = input.nextInt();

                    System.out.println();

                    teller.createCustomerAccount(accountTypeCustomer, usernameCustomer, passwordCustomer, nameCustomer, addressCustomer, phoneCustomer, accountBalanceCustomer, pinCustomer);
                } else if (menu == 2) {
                    System.out.println("========== Tutup rekening nasabah ==========");
                    System.out.println("Masukkan username nasabah: ");
                    String usernameCustomer = input.nextLine();

                    System.out.println();

                    teller.setIsActiveCustomerAccount(usernameCustomer, false);
                } else if (menu == 3) {
                    System.out.println("========== Buka rekening nasabah ==========");
                    System.out.println("Masukkan username nasabah: ");
                    String usernameCustomer = input.nextLine();

                    System.out.println();

                    teller.setIsActiveCustomerAccount(usernameCustomer, true);
                } else if (menu == 4) {
                    System.out.println("========== Deposit uang nasabah ==========");
                    System.out.println("Masukkan username nasabah: ");
                    String usernameCustomer = input.nextLine();
                    System.out.println("Masukkan uang yang akan di depositkan ke rekening nasabah: ");
                    BigDecimal depositAmount = BigDecimal.valueOf(input.nextLong());

                    System.out.println();

                    teller.depositBalanceToCustomerAccount(usernameCustomer, depositAmount);
                } else if (menu == 5) {
                    System.out.println("========== Ubah data nasabah ==========");
                    System.out.println("Masukkan username nasabah: ");
                    String usernameCustomer = input.nextLine();

                    Customer customer = new Customer(usernameCustomer);
                    if (customer.customerId != null) {
                        customer.getCustomerData();

                        System.out.println();

                        System.out.println("Keterangan: jika data tidak ingin di ubah silahkan kosongkan!");
                        System.out.println();

                        System.out.println("Masukkan tipe akun nasabah baru: " + '(' + customer.accountType + ')');
                        String newAccountTypeCustomer = input.nextLine();
                        if (Objects.equals(newAccountTypeCustomer, "")) {
                            newAccountTypeCustomer = customer.accountType;
                        }

                        System.out.println("Masukkan username nasabah baru: " + '(' + customer.username + ')');
                        String newUsernameCustomer = input.nextLine();
                        if (Objects.equals(newUsernameCustomer, "")) {
                            newUsernameCustomer = customer.username;
                        }

                        System.out.println("Masukkan password nasabah baru: " + '(' + "*******" + ')');
                        String newPasswordCustomer = input.nextLine();
                        if (Objects.equals(newPasswordCustomer, "")) {
                            newPasswordCustomer = customer.password;
                        }

                        System.out.println("Masukkan nama lengkap nasabah baru: " + '(' + customer.name + ')');
                        String newNameCustomer = input.nextLine();
                        if (Objects.equals(newNameCustomer, "")) {
                            newNameCustomer = customer.name;
                        }

                        System.out.println("Masukkan alamat lengkap nasabah baru: " + '(' + customer.address + ')');
                        String newAddressCustomer = input.nextLine();
                        if (Objects.equals(newAddressCustomer, "")) {
                            newAddressCustomer = customer.address;
                        }

                        System.out.println("Masukan nomor telefon nasabah baru: " + '(' + customer.phone + ')');
                        String newPhoneCustomer = input.nextLine();
                        if (Objects.equals(newPhoneCustomer, "")) {
                            newPhoneCustomer = customer.phone;
                        }

                        System.out.println("Masukkan pin nasabah baru: " + '(' + customer.pin + ')');
                        String newPinCustomer = input.nextLine();
                        if (Objects.equals(newPinCustomer, "")) {
                            newPinCustomer = customer.pin.toString();
                        }

                        teller.updateDataCustomerAccount(usernameCustomer, newAccountTypeCustomer, newUsernameCustomer, newPasswordCustomer, newNameCustomer, newAddressCustomer, newPhoneCustomer, Integer.parseInt(newPinCustomer));
                    } else {
                        System.out.println();
                        System.out.println("Data customer tidak ditemukan!");
                    }
                }
            } else if (Objects.equals(employee.accountType, "Manager")) {
                Manager manager = new Manager(username, password);
                manager.authenticate();
                manager.getEmployeeData();

                System.out.println();
                int menu = chooseManagerMenu();
                System.out.println();
            }
        } else {
            System.out.println("Akun anda tidak aktif atau di blokir!");
        }
    }

    public static Integer chooseCustomerMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih menu di bawah ini");
        System.out.println("1. Transfer");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Daftar Transaksi");

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
            if (Objects.equals(customer.accountType, "Silver")) {
                Silver silver = new Silver(username, password);
                silver.authenticate();
                silver.getCustomerData();

                System.out.println("Selamat datang Customer " + silver.name + "!");

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
            } else if (Objects.equals(customer.accountType, "Gold")) {
                Gold gold = new Gold(username, password);
                gold.authenticate();
                gold.getCustomerData();

                System.out.println("Selamat datang Customer " + gold.name + "!");

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
            } else if (Objects.equals(customer.accountType, "Platinum")) {
                Platinum platinum = new Platinum(username, password);
                platinum.authenticate();
                platinum.getCustomerData();

                System.out.println("Selamat datang Customer " + platinum.name + "!");

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