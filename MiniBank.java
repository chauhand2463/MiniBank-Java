import java.util.Scanner;

record BankInfo(String name, String branch) {
}

enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class MiniBank {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("  " + bank.name() + " - " + bank.branch());

        Account[] accounts = {
                new Account("Dhairy Chauhan", 5000),
                new Account("Goku"),
                new Account("Vegeta", 12000),
                new Account("Gojo Satoru", 25000)
        };

        accounts[0].deposit(2500);
        accounts[0].withdraw(1000);
        accounts[1].deposit(8000);
        accounts[1].withdraw(9000);
        accounts[2].withdraw(3000);

        System.out.println();
        for (Account a : accounts) {
            System.out.println(a);
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
        System.out.println("Mobile 9876543210 : " + Validator.isValidMobile("9876543210"));
        System.out.println("Mobile 12345      : " + Validator.isValidMobile("12345"));
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
