package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    Integer employeeId;
    String accountType;
    String username;
    String password;
    String name;
    String address;
    String phone;
    Boolean isActive;
    Connection conn = ConnectionManager.getInstance().getConnection();

    public Employee(String username, String password){
        this.username = username;
        this.password = password;
    }

//    public createCustomerBankAccount()

    private void getEmployeeData(){
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT DISTINCT ed.name, ed.phone, ed.address, ed.employee_is_active,\n" +
                            "                (SELECT employee_account_type FROM employee_account_type WHERE id_employee_account_type=ed.employee_account_type_id) AS account_type\n" +
                            "FROM employee_data ed\n" +
                            "WHERE ed.id_employee=?;"
            );

            ps.setInt(1, this.employeeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.accountType = rs.getString("account_type");
                this.name = rs.getString("name");
                this.address = rs.getString("address");
                this.phone = rs.getString("phone");
                this.isActive = rs.getBoolean("employee_is_active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void authenticate() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT el.employee_id, ed.employee_is_active\n" +
                            "FROM employee_login el, employee_data ed\n" +
                            "WHERE username=? AND password=? AND el.employee_id=ed.id_employee;"
            );

            ps.setString(1, this.username);
            ps.setString(2, this.password);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.employeeId = rs.getInt("employee_id");
                this.isActive = rs.getBoolean("employee_is_active");
            }

            if (this.isActive) {
                getEmployeeData();
                System.out.println("Selamat datang Employee " + this.name + "!");
            } else {
                System.out.println("Akun anda tidak aktif!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
