package main;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    Integer customerId;
    String accountType;
    Boolean isActive;
    Integer pin;
    String username;
    String password;
    String name;
    String address;
    String phone;
    BigDecimal balance;
    Connection conn = ConnectionManager.getInstance().getConnection();

    public Customer(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void getCustomerData(){
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT cd.name, cd.phone, cd.address, cba.account_balance,\n" +
                    "                (SELECT customer_account_type FROM customer_account_type WHERE customer_account_type.id_customer_account_type=cba.customer_account_type_id) AS account_type,\n" +
                    "                cba.customer_is_active, cba.account_pin\n" +
                    "FROM customer_data cd, customer_bank_account cba, customer_account_type cat\n" +
                    "WHERE cba.id_customer_bank_account=? AND cd.id_customer=?;");

            ps.setInt(1, this.customerId);
            ps.setInt(2, this.customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.name = rs.getString("name");
                this.address = rs.getString("address");
                this.phone = rs.getString("phone");
                this.balance = rs.getBigDecimal("account_balance");
                this.accountType = rs.getString("account_type");
                this.isActive = rs.getBoolean("customer_is_active");
                this.pin = rs.getInt("account_pin");
            }

            if (this.isActive) {
                System.out.println("Selamat datang Customer " + this.name + "!");
            } else {
                System.out.println("Akun anda tidak aktif!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void authenticate() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cl.customer_id, cba.customer_is_active,\n" +
                            "       (SELECT customer_account_type FROM customer_account_type WHERE id_customer_account_type=cba.customer_account_type_id) as account_type\n" +
                            "FROM customer_login cl,customer_bank_account cba\n" +
                            "WHERE username=? AND password=? AND cl.customer_id=cba.customer_id;"
            );

            ps.setString(1, this.username);
            ps.setString(2, this.password);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.customerId = rs.getInt("customer_id");
                this.isActive = rs.getBoolean("customer_is_active");
                this.accountType = rs.getString("account_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getBalanceAfterAddition(Integer customerId, BigDecimal amount) {
        BigDecimal remainingBalance = null;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT IF((cba.account_balance + ?) > cat.max_balance_limit = 1, -1, cba.account_balance + ?)\n" +
                    "    AS remaining_balance\n" +
                    "FROM customer_bank_account cba, customer_account_type cat\n" +
                    "WHERE cba.customer_id=? AND cat.id_customer_account_type=cba.customer_account_type_id;");

            ps.setBigDecimal(1, amount);
            ps.setBigDecimal(2, amount);
            ps.setInt(3, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                remainingBalance = rs.getBigDecimal("remaining_balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingBalance;
    }

    public BigDecimal getBalanceAfterSubtraction(Integer customerId, BigDecimal amount) {
        BigDecimal remainingBalance = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT IF(SIGN(cba.account_balance - ?) = -1, -1, cba.account_balance - ?)\n" +
                            "    AS remaining_balance\n" +
                            "FROM customer_bank_account cba\n" +
                            "WHERE cba.customer_id=?;"
            );

            ps.setBigDecimal(1, amount);
            ps.setBigDecimal(2, amount);
            ps.setInt(3, customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                remainingBalance = rs.getBigDecimal("remaining_balance");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingBalance;
    }

    public void updateBalance (Integer customerId, BigDecimal updatedBalance) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer_bank_account SET account_balance=? WHERE customer_id=?;"
            );

            ps.setBigDecimal(1, updatedBalance);
            ps.setInt(2, customerId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
