package main;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Silver extends Customer {
    BigDecimal maximumBalance;
    BigDecimal maximumTransfer;
    BigDecimal maximumDeposit;
    BigDecimal maximumWithdraw;
    Connection conn = ConnectionManager.getInstance().getConnection();

    public Silver(String username, String password) {
        super(username, password);

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cat.max_transfer_limit, cat.max_balance_limit, cat.max_deposit_limit, cat.max_withdraw_limit\n" +
                            "FROM customer_account_type cat\n" +
                            "WHERE cat.customer_account_type=?;"
            );

            ps.setString(1, "Silver");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.maximumBalance = rs.getBigDecimal("max_balance_limit");
                this.maximumTransfer = rs.getBigDecimal("max_transfer_limit");
                this.maximumDeposit = rs.getBigDecimal("max_deposit_limit");
                this.maximumWithdraw = rs.getBigDecimal("max_withdraw_limit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferToAnotherBankAccount (Integer receiverCustomerId, BigDecimal amount) {
        if (amount.compareTo(maximumTransfer) <= 0) {
            BigDecimal remainingBalanceSender = super.getBalanceAfterSubtraction(super.customerId, amount);
            BigDecimal remainingBalanceReceiver = super.getBalanceAfterAddition(receiverCustomerId, amount);

            if (remainingBalanceSender.compareTo(BigDecimal.valueOf(0)) > 0 && remainingBalanceReceiver.compareTo(BigDecimal.valueOf(0)) > 0) {
                updateBalance(super.customerId, remainingBalanceSender);
                updateBalance(receiverCustomerId, remainingBalanceReceiver);
                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", true, "Success");
                System.out.println("Berhasil melakukan transfer " + amount + " ke rekening " + receiverCustomerId);
                transaction.displayTransactionData();
            } else if (remainingBalanceSender.compareTo(BigDecimal.valueOf(0)) < 0) {
                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", false, "Saldo anda tidak cukup untuk melakukan transfer sebesar " + amount);
                System.out.println("Saldo anda tidak cukup untuk melakukan transfer sebesar " + amount);
            } else if (remainingBalanceReceiver.compareTo(BigDecimal.valueOf(0)) < 0) {
                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", false, "Rekening penerima telah mencapai batas saldo!");
                System.out.println("Rekening penerima telah mencapai batas saldo!");
            }
        } else {
            System.out.println("Maksimal transfer adalah " + this.maximumTransfer);
        }
    }
}
