import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        int menu = 1;
        int saldo = 100000;

        Scanner input = new Scanner(System.in);
        System.out.println("Selamat datang di ATM Bank Java");
        System.out.println("Masukkan username: ");
        String username = input.nextLine();
        System.out.println("Masukkan password: ");
        String password = input.nextLine();

        Login login = new Login(username, password);

        if(login.login()) {
            while(menu != 4) {
                System.out.println("Menu: ");
                System.out.println("1. Cek Saldo");
                System.out.println("2. Tarik Tunai");
                System.out.println("3. Transfer");
                System.out.println("4. Keluar");
                System.out.println("Pilih menu: ");
                menu = input.nextInt();

                if(menu == 1) {
                    System.out.println("Saldo anda: " + saldo);
                } else if(menu == 2) {
                    System.out.println("Masukkan jumlah uang yang akan ditarik: ");
                    int tarik = input.nextInt();
                    saldo = saldo - tarik;
                    System.out.println("Saldo anda: " + saldo);
                } else if(menu == 3) {
                    System.out.println("Masukkan jumlah uang yang akan ditransfer: ");
                    int transfer = input.nextInt();
                    saldo = saldo - transfer;
                    System.out.println("Saldo anda: " + saldo);
                } else if(menu == 4) {
                    System.out.println("Terima kasih telah menggunakan ATM Bank Java");
                } else {
                    System.out.println("Menu tidak tersedia");
                }
            }
        } else {
            System.out.println("Username atau password salah");
        }
    }
}