public class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Account Number : ").append(account.getAccountNumber()).append("\n");
        sb.append("Owner Name     : ").append(account.getOwnerName()).append("\n");
        sb.append("Balance        : Rs. ").append(account.getBalance()).append("\n");
        sb.append("Status         : ").append(account.isActive() ? "Active" : "Inactive").append("\n");

        return sb.toString();
    }
}
