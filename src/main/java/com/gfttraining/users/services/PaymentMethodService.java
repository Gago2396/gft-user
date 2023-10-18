package com.gfttraining.users.services;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

//    public PaymentMethod getPaymentMethodByName(long id, PaymentMethod paymentMethod) {
//        // ToDo: Implement GET logic here and return the PaymentMethod
//        return null;
//    }

    public Optional<PaymentMethod> getPaymentMethodByName(String paymentMethodName) {
        return paymentMethodRepository.findByName(paymentMethodName);
    }
}
