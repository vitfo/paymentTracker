package cz.vitfo.util;

import cz.vitfo.model.Payment;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputOutputUtilTest {

    @Test
    public void readInputTest() {
        final String validInput = "CZK 150";
        final String invalidInput1 = "usd 15";
        final String invalidInput2 = "US 15";
        final String invalidInput3 = "USD usd";
        final String invalidInput4 = "15 USD";

        final Optional<Payment> paymentCorrect = InputOutputUtil.readInput(validInput);
        assertTrue(paymentCorrect.isPresent());
        assertEquals("CZK", paymentCorrect.get().getCurrency());
        assertEquals(150, paymentCorrect.get().getAmount().intValue());

        assertFalse(InputOutputUtil.readInput(invalidInput1).isPresent());
        assertFalse(InputOutputUtil.readInput(invalidInput2).isPresent());
        assertFalse(InputOutputUtil.readInput(invalidInput3).isPresent());
        assertFalse(InputOutputUtil.readInput(invalidInput4).isPresent());
    }
}
