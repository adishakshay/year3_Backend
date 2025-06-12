package com.example.backend.controller;

import com.example.backend.model.Payment;
import com.example.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/payment/add")
    public ResponseEntity<Payment> addEvent(@RequestBody Payment pay) {

        Payment obj = paymentService.create(pay);       
        return new ResponseEntity<>(obj,HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/adminuser/payments/count")
    public ResponseEntity<Integer> getPaymentCount() {
        int count = paymentService.getAllPayments().size(); // Assuming getAllPayments() returns a list of payments
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/payment/getall")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return payment != null ? new ResponseEntity<>(payment, HttpStatus.OK)
                               : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/adminuser/adminpayment/{transaction}")
    public ResponseEntity<Boolean> deletePayment(@PathVariable("transaction") String transaction) {
        boolean deleted = paymentService.deletePayment(transaction);
        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}

