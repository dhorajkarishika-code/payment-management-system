
package com.ishika.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Search by status
    List<Payment> findByStatus(TransactionStatus status);

    // Search by amount greater than
    List<Payment> findByAmountGreaterThan(Integer amount);

}

