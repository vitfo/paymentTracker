package cz.vitfo.dao;

import cz.vitfo.model.Payment;

import java.util.List;

public interface PaymentDao {

    List<Payment> findAll();

    void save(Payment payment);

    void save(List<Payment> payments);

    void deleteAll();
}
