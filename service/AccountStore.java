package service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Account;

public class AccountStore {

    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public Collection<Account> getAll() {
        return accounts.values();
    }

    public void addTransaction(String line) {
        transactionHistory.add(line);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = new ArrayList<>(accounts.values());
        sorted.sort(Comparator.comparingLong(Account::getBalance)
                .thenComparing(Account::getAccountNumber));
        return sorted;
    }
}
