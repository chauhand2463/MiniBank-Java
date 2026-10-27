package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;

public class DeadlockDemo {

    public static void unsafeTransfer(Account from, Account to, long amount) throws BankException {
        synchronized (from) {
            pause();
            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public static void safeTransfer(Account from, Account to, long amount) throws BankException {
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            pause();
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    private static void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account a = new SavingsAccount("Deadlock A", 10000, 0);
        Account b = new SavingsAccount("Deadlock B", 10000, 0);

        Thread unsafeOne = new Thread(() -> {
            try {
                unsafeTransfer(a, b, 500);
            } catch (BankException e) {
                System.out.println("unsafe A->B failed: " + e.getMessage());
            }
        }, "unsafe-A-to-B");
        Thread unsafeTwo = new Thread(() -> {
            try {
                unsafeTransfer(b, a, 500);
            } catch (BankException e) {
                System.out.println("unsafe B->A failed: " + e.getMessage());
            }
        }, "unsafe-B-to-A");

        unsafeOne.setDaemon(true);
        unsafeTwo.setDaemon(true);
        unsafeOne.start();
        unsafeTwo.start();
        Thread.sleep(1000);
        System.out.println("Unsafe transfer deadlocked: " + (unsafeOne.isAlive() && unsafeTwo.isAlive()));

        Account c = new SavingsAccount("Safe C", 10000, 0);
        Account d = new SavingsAccount("Safe D", 10000, 0);

        Thread safeOne = new Thread(() -> {
            try {
                safeTransfer(c, d, 500);
            } catch (BankException e) {
                System.out.println("safe C->D failed: " + e.getMessage());
            }
        }, "safe-C-to-D");
        Thread safeTwo = new Thread(() -> {
            try {
                safeTransfer(d, c, 500);
            } catch (BankException e) {
                System.out.println("safe D->C failed: " + e.getMessage());
            }
        }, "safe-D-to-C");

        safeOne.start();
        safeTwo.start();
        safeOne.join();
        safeTwo.join();
        System.out.println("Safe balances: " + c.getBalance() + " / " + d.getBalance());
    }
}
