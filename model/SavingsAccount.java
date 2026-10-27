package model;

public class SavingsAccount extends Account {

    private final long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && getBalance() - amount >= minBalance;
    }

    @Override
    protected long availableBalance() {
        return getBalance() - minBalance;
    }
}
