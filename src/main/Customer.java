package main;

import java.math.BigDecimal;
import java.sql.*;

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

    public Customer(String username) {
        this.username = username;
        this.customerId = getCustomerId(username);
    }

    public Customer(String accountType, String username, String password, String name, String address, String phone, BigDecimal accountBalance, Integer pin) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer_data (name, address, phone) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal menambahkan data!");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer newCustomerId = generatedKeys.getInt(1);
                    Integer idAccountType =  getIdAccountType(accountType);
                    if (idAccountType != null) {
                        this.customerId = newCustomerId;
                        createCustomerBankAccount(newCustomerId, idAccountType, accountBalance, pin);
                        createCustomerLogin(newCustomerId, username, password);
                    } else {
                        System.out.println("Id account type tidak di temukan!");
                    }
                }
                else {
                    throw new SQLException("Gagal mendapatkan id!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerData (String name, String address, String phone) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer_data cd SET cd.name=?, cd.address=?, cd.phone=? WHERE cd.id_customer=?;",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);
            ps.setInt(4, this.customerId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal mengupdate data!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerBankAccount (String accountType, Integer pin) {
        Integer idAccountType = getIdAccountType(accountType);

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer_bank_account cba SET cba.customer_account_type_id=?, cba.account_pin=? WHERE cba.customer_id=?;",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, idAccountType);
            ps.setInt(2, pin);
            ps.setInt(3, this.customerId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal mengupdate data!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCustomerBankAccount (Integer customerId, Integer accountType, BigDecimal accountBalance, Integer pin) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer_bank_account (customer_id, customer_account_type_id, account_balance, account_pin) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, customerId);
            ps.setInt(2, accountType);
            ps.setBigDecimal(3, accountBalance);
            ps.setInt(4, pin);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal menambahkan data!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerLogin (String username, String password) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer_login cl SET cl.username=?, cl.password=? WHERE customer_id=?;",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, this.customerId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal mengupdate data!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCustomerLogin (Integer customerId, String username, String password) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer_login (username, password, customer_id) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, customerId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal menambahkan data!");
            }

            this.username = username;
            this.password = password;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCustomerData(){
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT cl.username, cl.password, cd.name, cd.phone, cd.address, cba.account_balance,\n" +
                    "                (SELECT customer_account_type FROM customer_account_type WHERE customer_account_type.id_customer_account_type=cba.customer_account_type_id) AS account_type,\n" +
                    "                cba.customer_is_active, cba.account_pin\n" +
                    "FROM customer_data cd, customer_bank_account cba, customer_account_type cat, customer_login cl\n" +
                    "WHERE cd.id_customer=? AND cba.customer_id=cd.id_customer AND cat.id_customer_account_type=cba.customer_account_type_id AND cl.customer_id= cd.id_customer;");

            ps.setInt(1, this.customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                this.name = rs.getString("name");
                this.address = rs.getString("address");
                this.phone = rs.getString("phone");
                this.balance = rs.getBigDecimal("account_balance");
                this.accountType = rs.getString("account_type");
                this.isActive = rs.getBoolean("customer_is_active");
                this.pin = rs.getInt("account_pin");
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

    public Integer getCustomerId(String username) {
        Integer customerId = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cl.customer_id FROM customer_login cl WHERE cl.username=?;"
            );

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customerId = rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerId;
    }

    public Boolean getIsActive(Integer customerId) {
        Boolean isActive = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cba.customer_is_active FROM customer_bank_account cba WHERE cba.customer_id=?;"
            );

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                isActive = rs.getBoolean("customer_is_active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isActive;
    }

    public Integer getIdAccountType (String accountType) {
        Integer idAccountType = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cat.id_customer_account_type FROM customer_account_type cat WHERE cat.customer_account_type=?;"
            );

            ps.setString(1, accountType);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idAccountType = rs.getInt("id_customer_account_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idAccountType;
    }

    public Boolean isCustomerPinValid (Integer customerId, Integer pin) {
        Boolean isValid = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cat.id_customer_account_type FROM customer_account_type cat WHERE cat.customer_account_type=?;"
            );

            ps.setString(1, accountType);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                isValid = rs.getBoolean("id_customer_account_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public void setIsActive (Boolean isActive) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer_bank_account SET customer_is_active=? WHERE customer_id=?"
            );

            ps.setBoolean(1, isActive);
            ps.setInt(2, this.customerId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal menambahkan data!");
            } else {
                this.isActive = isActive;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void  displayCustomerData () {
        System.out.println("customerId: " + this.customerId);
        System.out.println("accountType: " + this.accountType);
        System.out.println("isActive: " + this.isActive);
        System.out.println("pin: " + this.pin);
        System.out.println("username: " + this.username);
        System.out.println("password: " + this.password);
        System.out.println("name: " + this.name);
        System.out.println("address: " + this.address);
        System.out.println("phone: " + this.phone);
        System.out.println("balance: " + this.balance);
    }
}
