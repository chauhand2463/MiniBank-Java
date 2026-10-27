package util;

import model.Command;
import model.TransactionType;

public class CommandParser {

    public static Command parse(String line) {
        if (line == null) {
            return null;
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            return null;
        }
        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
        long amount;
        try {
            amount = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Command(type, parts[1].toUpperCase(), amount);
    }
}
