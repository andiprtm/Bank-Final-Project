package main;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    Integer customerId;
    String username;
    String password;
    String name;
    String address;
    String phone;
    BigDecimal balance;

    public Customer(String password, String username){
        this.password = password;
        this.username = username;
    }

    public void transfer () {

    }

    public BigDecimal getBalance () {
        return this.balance;
    }

    public void withDraw () {

    }

    public void viewTransaction () {

    }

    public void getCustomerData(){
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.name, c.address, c.phone, c.balance FROM customer c WHERE id=?;"
            );

            ps.setInt(1, this.customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.name = rs.getString("name");
                this.address = rs.getString("address");
                this.phone = rs.getString("phone");
                this.balance = rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void authenticate() {
        Login auth = new Login();
        auth.setUsername(this.username);
        auth.setPassword(this.password);
        auth.setRole("customer");

        if (auth.authenticate()){
            System.out.println("selamat cok login berhasil");
            this.customerId = auth.getUserId();
            getCustomerData();
        } else {
            System.out.println("loginmu salah cok");
        }
    }
}
