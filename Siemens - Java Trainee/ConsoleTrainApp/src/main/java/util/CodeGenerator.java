package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CodeGenerator {
    private CodeGenerator() {
    }

    public static String generateBookingCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String randomPart = UUID.randomUUID().toString()
                .substring(0, 8)
                .toUpperCase();

        return "BK-" + date + "-" + randomPart;
    }

    public static String generateTicketCode() {
        String randomPart = UUID.randomUUID().toString()
                .substring(0, 10)
                .toUpperCase();

        return "TCK-" + randomPart;
    }
}