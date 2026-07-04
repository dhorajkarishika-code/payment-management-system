package com.ishika.payment;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ishika.payment.dto.PaymentRequestDTO;
import com.ishika.payment.dto.PaymentResponseDTO;

@Service
public class PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository repository;

    // Convert Entity -> DTO
    private PaymentResponseDTO convertToDTO(Payment payment) {

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
    }

    // CREATE
    public PaymentResponseDTO save(PaymentRequestDTO dto) {

        Payment payment = new Payment();

        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());

        Payment saved = repository.save(payment);

        logger.info("Payment created successfully. ID: {}, Amount: {}, Status: {}",
                saved.getId(),
                saved.getAmount(),
                saved.getStatus());

        return convertToDTO(saved);
    }

    // GET ALL
    public List<PaymentResponseDTO> getAll() {

        logger.info("Fetching all payments");

        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PAGINATION + SORTING
    public Page<PaymentResponseDTO> getPayments(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        logger.info("Fetching page {} size {} sorted by {}", page, size, sortBy);

        return repository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // GET BY ID
    public PaymentResponseDTO getById(int id) {

        Payment payment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + id));

        logger.info("Fetching payment {}", id);

        return convertToDTO(payment);
    }

    // UPDATE
    public PaymentResponseDTO update(int id, PaymentRequestDTO dto) {

        Payment payment = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with ID: " + id));

        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());

        Payment updated = repository.save(payment);

        logger.info("Payment updated successfully. ID: {}", id);

        return convertToDTO(updated);
    }

    // DELETE
    public void delete(int id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Payment not found with ID: " + id);
        }

        logger.info("Deleting payment {}", id);

        repository.deleteById(id);
    }

    // SEARCH BY STATUS
    public List<PaymentResponseDTO> getByStatus(TransactionStatus status) {

        logger.info("Searching payments by status {}", status);

        return repository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // SEARCH BY AMOUNT
    public List<PaymentResponseDTO> getByAmount(Integer amount) {

        logger.info("Searching payments greater than {}", amount);

        return repository.findByAmountGreaterThan(amount)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}