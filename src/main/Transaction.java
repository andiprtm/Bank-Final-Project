package main;

import java.math.BigDecimal;
import java.sql.*;

public class Transaction {
    Integer transactionId;
    Timestamp transactionDate;
    Integer transactionFor;
    Integer transactionFrom;
    BigDecimal transactionAmount;
    String transactionType;
    Boolean transactionStatus;
    String transactionMessage;
    BigDecimal transactionAdminFee;
    Connection conn = ConnectionManager.getInstance().getConnection();

    public Transaction() {

    }

    private Integer getIdTransactionType (String transactionType) {
        Integer idTransactionType = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT tt.id_transaction_type\n" +
                            "FROM transaction_type tt\n" +
                            "WHERE tt.transaction_type=?;"
            );

            ps.setString(1, transactionType);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idTransactionType=rs.getInt("id_transaction_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idTransactionType;
    }

    public void createTransaction(Integer transactionFor, Integer transactionFrom, BigDecimal transactionAmount, String transactionType, Boolean transactionStatus, String transactionMessage, BigDecimal transactionAdminFee) {
        Integer idTransactionType = getIdTransactionType(transactionType);
        if (idTransactionType != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO transaction_history (transaction_for, transaction_from, transaction_amount, transaction_type_id, transaction_status, transaction_message, transaction_admin_fee) VALUES (?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setInt(1, transactionFor);
                ps.setInt(2, transactionFrom);
                ps.setBigDecimal(3, transactionAmount);
                ps.setInt(4, idTransactionType);
                ps.setBoolean(5, transactionStatus);
                ps.setString(6, transactionMessage);
                ps.setBigDecimal(7, transactionAdminFee);

                int affectedRows = ps.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        getTransactionData(generatedKeys.getInt(1));
                    }
                    else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Tipe transaksi tidak ditemukan!");
        }
    }

    public void getTransactionData(Integer transactionId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT th.id_transaction_history, th.transaction_date, th.transaction_for, th.transaction_from, th.transaction_amount, tt.transaction_type, th.transaction_status, th.transaction_message, th.transaction_admin_fee\n" +
                    "FROM transaction_history th, transaction_type tt\n" +
                    "WHERE id_transaction_history=? AND th.transaction_type_id=tt.id_transaction_type;"
            );

            ps.setInt(1, transactionId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.transactionId = rs.getInt("id_transaction_history");
                this.transactionDate = rs.getTimestamp("transaction_date");
                this.transactionFor = rs.getInt("transaction_for");
                this.transactionFrom = rs.getInt("transaction_from");
                this.transactionAmount = rs.getBigDecimal("transaction_amount");
                this.transactionType = rs.getString("transaction_type");
                this.transactionStatus = rs.getBoolean("transaction_status");
                this.transactionMessage = rs.getString("transaction_message");
                this.transactionAdminFee = rs.getBigDecimal("transaction_admin_fee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayTransactionData() {
        System.out.println(this.transactionId);
        System.out.println(this.transactionDate);
        System.out.println(this.transactionFor);
        System.out.println(this.transactionFrom);
        System.out.println(this.transactionAmount);
        System.out.println(this.transactionType);
        System.out.println(this.transactionStatus);
        System.out.println(this.transactionMessage);
        System.out.println(this.transactionAdminFee);
    }
}
