package main;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Platinum extends Customer {
    BigDecimal maximumBalance;
    BigDecimal maximumTransfer;
    BigDecimal maximumDeposit;
    BigDecimal maximumWithdraw;
    double adminFee;
    Connection conn = ConnectionManager.getInstance().getConnection();

    public Platinum(String username, String password) {
        super(username, password);

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cat.max_transfer_limit, cat.max_balance_limit, cat.max_deposit_limit, cat.max_withdraw_limit, cat.admin_fee\n" +
                            "FROM customer_account_type cat\n" +
                            "WHERE cat.customer_account_type=?;"
            );

            ps.setString(1, "Platinum");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.maximumBalance = rs.getBigDecimal("max_balance_limit");
                this.maximumTransfer = rs.getBigDecimal("max_transfer_limit");
                this.maximumDeposit = rs.getBigDecimal("max_deposit_limit");
                this.maximumWithdraw = rs.getBigDecimal("max_withdraw_limit");
                this.adminFee = rs.getDouble("admin_fee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferToAnotherBankAccount (Integer receiverCustomerId, BigDecimal amount) {
        BigDecimal adminFeeTotal = amount.multiply(BigDecimal.valueOf(adminFee));
        BigDecimal amountAfterAdminFee = amount.add(adminFeeTotal);

        if (amountAfterAdminFee.compareTo(maximumTransfer) <= 0) {

            BigDecimal remainingBalanceSender = super.getBalanceAfterSubtraction(super.customerId, amountAfterAdminFee);
            BigDecimal remainingBalanceReceiver = super.getBalanceAfterAddition(receiverCustomerId, amountAfterAdminFee);

            if (remainingBalanceSender.compareTo(BigDecimal.valueOf(0)) > 0 && remainingBalanceReceiver.compareTo(BigDecimal.valueOf(0)) > 0) {

                updateBalance(super.customerId, remainingBalanceSender);
                updateBalance(receiverCustomerId, remainingBalanceReceiver);
                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", true, "Sukses melakukan transfer!", adminFeeTotal);
                System.out.println("Berhasil melakukan transfer " + amount  + " dengan biaya admin sebesar " + adminFeeTotal + " ke rekening " + receiverCustomerId);

            } else if (remainingBalanceSender.compareTo(BigDecimal.valueOf(0)) < 0) {

                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", false, "Saldo anda tidak cukup untuk melakukan transfer sebesar " + amount + " + biaya admin sebesar " + adminFeeTotal, adminFeeTotal);
                System.out.println("Saldo anda tidak cukup untuk melakukan transfer sebesar " + amount + " + biaya admin sebesar " + adminFeeTotal);

            } else if (remainingBalanceReceiver.compareTo(BigDecimal.valueOf(0)) < 0) {

                Transaction transaction = new Transaction();
                transaction.createTransaction(receiverCustomerId, super.customerId, amount, "Transfer", false, "Rekening penerima telah mencapai batas saldo!", adminFeeTotal);
                System.out.println("Rekening penerima telah mencapai batas saldo!");

            }
        } else {
            System.out.println("Maksimal transfer adalah " + this.maximumTransfer);
        }
    }

    public void withdrawBalance (BigDecimal amount) {
        BigDecimal adminFeeTotal = amount.multiply(BigDecimal.valueOf(adminFee));
        BigDecimal amountAfterAdminFee = amount.add(adminFeeTotal);

        BigDecimal remainingBalance = super.getBalanceAfterSubtraction(super.customerId, amountAfterAdminFee);

        if (amount.compareTo(maximumWithdraw) <= 0) {

            if (remainingBalance.compareTo(BigDecimal.valueOf(0)) > 0) {

                updateBalance(super.customerId, remainingBalance);
                Transaction transaction = new Transaction();
                transaction.createTransaction(super.customerId, super.customerId, amount, "Withdraw", true, "Sukses melakukan penarikan!", adminFeeTotal);
                System.out.println("Berhasil melakukan penarikan sebesar " + amount + " dengan biaya admin sebesar " + adminFeeTotal);

            } else {

                Transaction transaction = new Transaction();
                transaction.createTransaction(super.customerId, super.customerId, amount, "Withdraw", false, "Saldo anda tidak cukup untuk melakukan penarikan sebesar " + amount + " + biaya admin sebesar " + adminFeeTotal, adminFeeTotal);
                System.out.println("Saldo anda tidak cukup untuk melakukan penarikan sebesar " + amount + " + biaya admin sebesar " + adminFeeTotal);

            }

        } else {

            System.out.println("Maksimal penarikan adalah " + this.maximumWithdraw);

        }
    }

    public void depositBalance (BigDecimal amount) {
        BigDecimal remainingBalance = super.getBalanceAfterAddition(super.customerId, amount);

        if (amount.compareTo(maximumDeposit) <= 0) {

            updateBalance(super.customerId, remainingBalance);
            Transaction transaction = new Transaction();
            transaction.createTransaction(super.customerId, super.customerId, amount, "Deposit", true, "Sukses melakukan deposit!", BigDecimal.valueOf(0));
            System.out.println("Sukses melakukan deposit sejumlah " + remainingBalance);

        } else {

            System.out.println("Maksimal deposit adalah " + this.maximumDeposit);

        }
    }
}
