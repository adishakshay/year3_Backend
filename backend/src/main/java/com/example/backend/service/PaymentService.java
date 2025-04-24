package com.example.backend.service;

import com.example.backend.model.Payment;
import com.example.backend.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentRepo paymentRepo;

    public Payment create(Payment pay) {
        return paymentRepo.save(pay);
    }

    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepo.findById(paymentId).orElse(null);
    }

    public boolean deletePayment(String transaction) {
        Payment payment = paymentRepo.findByTransaction(transaction);
        if (payment != null) {
            paymentRepo.delete(payment);
            return true;
        }
        return false;
    }
}
