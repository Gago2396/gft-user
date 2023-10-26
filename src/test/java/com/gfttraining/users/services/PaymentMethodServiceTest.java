package com.gfttraining.users.services;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.services.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentMethodServiceTest {

    private PaymentMethodService paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    void setUp() {
        paymentMethodRepository = mock(PaymentMethodRepository.class);
        paymentMethodService = new PaymentMethodService(paymentMethodRepository);
    }

    @Test
    @DisplayName("GIVEN a valid PaymentMethod name name WHEN getPaymentMethodByName method is called THEN get a PaymentMethod")
    void testGetPaymentMethodByName() {
        String paymentMethodName = "PayPal";

        PaymentMethod expectedPaymentMethod = new PaymentMethod(1L, "PayPal");

        when(paymentMethodRepository.findByName(paymentMethodName)).thenReturn(Optional.of(expectedPaymentMethod));

        Optional<PaymentMethod> result = paymentMethodService.getPaymentMethodByName(paymentMethodName);

        assertEquals(Optional.of(expectedPaymentMethod), result);
    }
}
