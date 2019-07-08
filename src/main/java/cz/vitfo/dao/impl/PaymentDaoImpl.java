package cz.vitfo.dao.impl;

import cz.vitfo.dao.PaymentDao;
import cz.vitfo.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {

    private static final List<Payment> memory = new ArrayList<>();

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(memory);
    }

    @Override
    public void save(Payment payment) {
        memory.add(payment);
    }

    @Override
    public void save(List<Payment> payments) {
        memory.addAll(payments);
    }

    @Override
    public void deleteAll() {
        memory.clear();
    }
}
