package com.jumia.PaymentService.service;

import com.jumia.PaymentService.model.PaymentRequest;
import com.jumia.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
