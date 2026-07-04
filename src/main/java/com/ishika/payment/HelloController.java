package com.ishika.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishika.payment.dto.PaymentRequestDTO;
import com.ishika.payment.dto.PaymentResponseDTO;

import jakarta.validation.Valid;

@RestController
public class HelloController {

    @Autowired
    private PaymentService service;

    @GetMapping("/")
    public String home() {
        return "Payment Server Running 🚀";
    }

    @PostMapping("/pay")
    public PaymentResponseDTO createPayment(
            @Valid @RequestBody PaymentRequestDTO dto) {

        return service.save(dto);
    }

    @GetMapping("/payments")
    public List<PaymentResponseDTO> getPayments() {
        return service.getAll();
    }

    @GetMapping("/payments/page")
    public Page<PaymentResponseDTO> getPaymentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return service.getPayments(page, size, sortBy);
    }

    @GetMapping("/payments/{id}")
    public PaymentResponseDTO getPayment(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/payments/status/{status}")
    public List<PaymentResponseDTO> getByStatus(
            @PathVariable TransactionStatus status) {

        return service.getByStatus(status);
    }

    @GetMapping("/payments/amount/{amount}")
    public List<PaymentResponseDTO> getByAmount(
            @PathVariable Integer amount) {

        return service.getByAmount(amount);
    }

    @PutMapping("/payments/{id}")
    public PaymentResponseDTO updatePayment(
            @PathVariable int id,
            @Valid @RequestBody PaymentRequestDTO dto) {

        return service.update(id, dto);
    }

    @DeleteMapping("/payments/{id}")
    public String deletePayment(@PathVariable int id) {

        service.delete(id);

        return "Payment Deleted Successfully";
    }
}