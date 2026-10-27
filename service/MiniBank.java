package service;

import java.util.Scanner;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.BankInfo;
import model.Command;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import model.WithdrawRule;
import util.AnnotationValidator;
import util.CommandParser;
import util.StatementFormatter;
import util.TransactionLogger;
import util.Validator;

import static util.Validator.isValidMobile;

public class MiniBank {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("  " + bank.name() + " - " + bank.branch());

        Account[] accounts = {
                new SavingsAccount("Dhairy Chauhan", 5000, 1000),
                new CurrentAccount("Goku", 2000, 5000),
                new FixedDepositAccount("Vegeta", 25000),
                new SavingsAccount("Gojo Satoru", 12000, 5000)
        };

        try {
            accounts[0].deposit(2500);
            accounts[0].withdraw(1000);
            accounts[1].deposit(8000);
            accounts[2].withdraw(3000);
        } catch (InsufficientFundsException | InvalidAmountException e) {
            System.out.println("Operation failed: " + e.getMessage());
        } finally {
            System.out.println("Initial transaction batch finished");
        }

        System.out.println();
        for (Account a : accounts) {
            System.out.println(a);
        }

        System.out.println();
        for (Account a : accounts) {
            System.out.println(a.getOwnerName() + " | " + a.interestRate() + "% interest"
                    + " | yearly interest: " + a.yearlyInterest());
            if (a instanceof SavingsAccount sa) {
                System.out.println("   Savings: min balance " + sa.getMinBalance()
                        + ", canWithdraw(2000) -> " + sa.canWithdraw(2000));
            } else if (a instanceof CurrentAccount ca) {
                System.out.println("   Current: overdraft limit " + ca.getOverdraftLimit()
                        + ", canWithdraw(6000) -> " + ca.canWithdraw(6000));
            } else if (a instanceof FixedDepositAccount fd) {
                System.out.println("   FD (Premium): locked, canWithdraw(1) -> " + fd.canWithdraw(1));
            }
        }

        WithdrawRule noNegative = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount > 0 && account.getBalance() - amount >= 0;
            }
        };
        WithdrawRule byType = (account, amount) -> account.canWithdraw(amount);

        System.out.println();
        for (Account a : accounts) {
            System.out.println(a.getOwnerName() + " | anonymous rule(2000): " + noNegative.allow(a, 2000)
                    + " | lambda rule(2000): " + byType.allow(a, 2000));
        }

        System.out.println();
        System.out.println("accounts[0].equals(accounts[0]) -> " + accounts[0].equals(accounts[0]));
        System.out.println("accounts[0].equals(accounts[1]) -> " + accounts[0].equals(accounts[1]));

        Object obj = accounts[0];
        if (obj instanceof Account) {
            System.out.println("obj is an Account instance");
        }
        Customer vegita = new Customer("Vegeta", "vegeta@minibank.com", "9000000001",
                new Customer.Address("Capsule Corp", "West City", "110086"));
        Customer vegitaCopy = vegita.clone();
        System.out.println();
        System.out.println("Original : " + vegita.getName() + " | " + vegita.getCustomerId()
                + " | " + vegita.getAddress().getCity());
        System.out.println("Cloned   : " + vegitaCopy.getName() + " | " + vegitaCopy.getCustomerId());

        System.out.println();
        System.out.println("Mobile 9876543210 : " + isValidMobile("9876543210"));
        System.out.println("Mobile 12345      : " + isValidMobile("12345"));
        System.out.println("Email goku@dbz.com : " + Validator.isValidEmail("goku@dbz.com"));
        System.out.println("Email goku@dbz    : " + Validator.isValidEmail("goku@dbz"));
        System.out.println("PAN ABCDE1234F    : " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN abcde1234f    : " + Validator.isValidPan("abcde1234f"));
        System.out.println("IFSC SBIN0001234  : " + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("IFSC SBINXXX1234  : " + Validator.isValidIfsc("SBINXXX1234"));

        Command cmd = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println();
        System.out.println("Type    : " + cmd.type());
        System.out.println("Account : " + cmd.accountNumber());
        System.out.println("Amount  : " + cmd.amount());

        System.out.println();
        System.out.print(StatementFormatter.buildStatement(accounts[0]));

        SavingsAccount invalidAccount = new SavingsAccount("Validation Demo", -500, 1000);
        String[] validationErrors = AnnotationValidator.validate(invalidAccount);
        System.out.println("Annotation validation errors:");
        for (String error : validationErrors) {
            System.out.println(" - " + error);
        }

        System.out.println();
        try {
            accounts[0].withdraw(100000);
        } catch (InsufficientFundsException | InvalidAmountException e) {
            System.out.println("Withdrawal failed: " + e.getMessage()
                    + (e instanceof InsufficientFundsException ife
                            ? " | shortfall: " + ife.getShortfall() : ""));
        } finally {
            System.out.println("Withdrawal attempt complete");
        }

        try {
            accounts[0].deposit(-50);
        } catch (InvalidAmountException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }

        try {
            accounts[0].transfer(accounts[1], 1500);
            System.out.println("Transfer successful");
        } catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        try {
            accounts[0].transfer(null, 100);
        } catch (AccountNotFoundException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        try (TransactionLogger logger = new TransactionLogger("minibank-audit")) {
            logger.log("Account " + accounts[0].getAccountNumber()
                    + " balance " + accounts[0].getBalance());
        }

        System.out.println();
        Account threadDemo = new SavingsAccount("Thread Demo", 0, 0);
        Thread[] workers = new Thread[10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new AccountWorker(threadDemo, 1000, 1), "worker-" + (i + 1));
            workers[i].start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
        System.out.println("Final balance: " + threadDemo.getBalance() + " (expected 10000)");

        int choice = 0;

        while (choice != 5) {
            System.out.println();
            System.out.println("1. " + MenuOption.OPEN_ACCOUNT);
            System.out.println("2. " + MenuOption.DEPOSIT);
            System.out.println("3. " + MenuOption.WITHDRAW);
            System.out.println("4. " + MenuOption.TRANSFER);
            System.out.println("5. " + MenuOption.EXIT);
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice, try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT -> System.out.println("Open Account - to be implemented in a later lab");
                case DEPOSIT -> System.out.println("Deposit - to be implemented in a later lab");
                case WITHDRAW -> System.out.println("Withdraw - to be implemented in a later lab");
                case TRANSFER -> System.out.println("Transfer - to be implemented in a later lab");
                case EXIT -> System.out.println("Goodbye! Thank you for using " + bank.name() + ".");
            }
        }

        sc.close();
    }
}
