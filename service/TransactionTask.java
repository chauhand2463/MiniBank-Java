package service;

import exception.BankException;
import model.Account;
import model.TransactionType;

public class TransactionTask implements Runnable {

    private final Account account;
    private final TransactionType type;
    private final long amount;

    public TransactionTask(Account account, TransactionType type, long amount) {
        if (type == TransactionType.TRANSFER) {
            throw new IllegalArgumentException("TransactionTask supports DEPOSIT and WITHDRAW only");
        }
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            if (type == TransactionType.DEPOSIT) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
            System.out.println(Thread.currentThread().getName() + " " + type + " " + amount
                    + " on " + account.getAccountNumber() + " -> balance " + account.getBalance());
        } catch (BankException e) {
            System.out.println(Thread.currentThread().getName() + " " + type + " " + amount
                    + " failed: " + e.getMessage());
        }
    }
}
