package cz.vitfo.dao.impl;

import cz.vitfo.dao.PaymentDao;
import cz.vitfo.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentDaoImplTest {

    private PaymentDao paymentDao = new PaymentDaoImpl();

    @BeforeEach
    public void init() {
        paymentDao.deleteAll();
    }

    @Test
    public void findAllTest() {
        paymentDao.save(new Payment("EUR", new BigDecimal("200")));
        assertEquals(1, paymentDao.findAll().size());
    }

    @Test
    public void savePaymentTest() {
        paymentDao.save(new Payment("USD", new BigDecimal("150")));
        final List<Payment> all = paymentDao.findAll();
        assertEquals(1, all.size());
        assertEquals("USD", all.get(0).getCurrency());
        assertEquals(150, all.get(0).getAmount().intValue());
    }

    @Test
    public void saveListTest() {
        final List<Payment> payments = Arrays.asList(
                new Payment("CZK", new BigDecimal("750")),
                new Payment("USD", new BigDecimal("400")),
                new Payment("EUR", new BigDecimal("570"))
        );
        paymentDao.save(payments);
        assertEquals(3, paymentDao.findAll().size());
    }
}
