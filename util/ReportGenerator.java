package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generate(Path logsDir, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        StringBuilder details = new StringBuilder();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (Path file : stream) {
                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);
                details.append(file.getFileName())
                        .append(" | size=").append(attributes.size())
                        .append(" bytes | modified=").append(attributes.lastModifiedTime())
                        .append("\n");

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 2) {
                            continue;
                        }
                        try {
                            long amount = Long.parseLong(parts[1]);
                            if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                totalWithdrawals += amount;
                            }
                        } catch (NumberFormatException e) {
                            details.append("  skipped malformed line: ").append(line).append("\n");
                        }
                    }
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("========== End of Day Report ==========\n");
        report.append("Log files:\n").append(details);
        report.append("Total deposits    : ").append(totalDeposits).append("\n");
        report.append("Total withdrawals : ").append(totalWithdrawals).append("\n");
        report.append("Net flow          : ").append(totalDeposits - totalWithdrawals).append("\n");
        report.append("=======================================\n");

        Files.writeString(reportFile, report.toString());
    }
}
