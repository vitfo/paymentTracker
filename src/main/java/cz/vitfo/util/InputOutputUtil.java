package cz.vitfo.util;

import cz.vitfo.Application;
import cz.vitfo.model.Payment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public final class InputOutputUtil {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    private static final String VALID_INPUT = "[A-Z]{3} \\d*";

    private InputOutputUtil() {}

    /**
     * Saves list of payments to the file.
     *
     * @param payments list of payments to be saved
     */
    public static void saveListToFile(List<Payment> payments) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("memory.txt")), Charset.forName("UTF-8")))) {
            for (Payment payment : payments) {
                bw.write(payment.toString());
                bw.newLine();
            }
        } catch (IOException ioe) {
            LOGGER.severe(ioe.getMessage());
        }
    }

    /**
     * Reads file to the list.
     *
     * @param fileName of the file
     * @return list of payments from the file
     */
    public static List<Payment> readFileToList(String fileName) {
        final List<Payment> payments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8")))) {
            String line;
            while ((line = br.readLine()) != null) {
                final Optional<Payment> payment = readInput(line);
                payment.ifPresent(payments::add);
            }
        } catch (IOException ioe) {
            LOGGER.severe(ioe.getMessage());
        }
        return payments;
    }

    /**
     * Precessed the input.
     *
     * @param input input
     * @return optional payment if the input was valid or empty
     */
    public static Optional<Payment> readInput(String input) {
        if (isInputValid(input)) {
            final String[] split = input.split(" ");
            final Payment payment = new Payment(split[0], new BigDecimal(split[1]));
            return Optional.of(payment);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Prints payments.
     * Payments are printed only if amount > 0.
     *
     * @param payments list to be printed
     */
    public static void printListToOutput(List<Payment> payments) {
        if (payments.isEmpty()) {
            System.out.println("No payments");
        } else {
            payments.forEach(payment -> {
                if (payment.getAmount().intValue() != 0) {
                    System.out.println(payment);
                }
            });
        }
    }

    private static boolean isInputValid(String input) {
        return input.matches(VALID_INPUT);
    }
}
