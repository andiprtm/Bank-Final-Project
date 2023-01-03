package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    Integer userId;
    String username;
    String password;
    String role;
    boolean isLoggedIn;

    public Login() {

    }

    public void setRole(String role) { this.role = role; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() { return this.userId; }

    public boolean authenticate() {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT l.customer_id FROM login l WHERE username=? AND password=? AND role=?;"
            );

            ps.setString(1, this.username);
            ps.setString(2, this.password);
            ps.setString(3, this.role);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.isLoggedIn = true;
                this.userId = rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            this.isLoggedIn = false;
            e.printStackTrace();
        }

        return this.isLoggedIn;
    }
}
