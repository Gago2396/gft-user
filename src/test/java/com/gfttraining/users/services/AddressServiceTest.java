package com.gfttraining.users.services;

import com.gfttraining.users.models.Address;
import com.gfttraining.users.models.AddressRequest;
import com.gfttraining.users.models.Country;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.repositories.AddressRepository;
import com.gfttraining.users.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    private Country country;

    private Address address;

    private AddressRequest addressRequest;


    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);

        country = new Country(1L, "Spain");

        // Address
        address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setProvince("Province");
        address.setPostalCode(12345);
        address.setCountry(country);

        // Address Request
        addressRequest = new AddressRequest();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setProvince("Province");
        address.setPostalCode(12345);
        address.setCountry(country);
    }

    @Test
    void testAddAddress() {

        when(addressRepository.save(address)).thenReturn(address);

        Address savedAddress = addressService.addAddress(address);

        verify(addressRepository, times(1)).save(address);
        assertEquals(address, savedAddress);
    }

    @Test
    void testParseAddress() {

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Antonio");
        userRequest.setLastName("Garcia");
        userRequest.setStreet("23 Mayor");
        userRequest.setCity("Valencia");
        userRequest.setProvince("Valencia");
        userRequest.setPostalCode(46002);
        userRequest.setCountry("Spain");
        userRequest.setPaymentMethod("Credit Card");
        userRequest.setFidelityPoints(300);
        userRequest.setAveragePurchase(120.0);

        AddressRequest addressRequestToParse = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), country);

        Address parsedAddress = addressService.parseAddress(addressRequestToParse);

        assertEquals("23 Mayor", parsedAddress.getStreet());
        assertEquals("Valencia", parsedAddress.getCity());
        assertEquals("Valencia", parsedAddress.getProvince());
        assertEquals(46002, parsedAddress.getPostalCode());
        assertEquals("Spain", parsedAddress.getCountry().getName());
    }
}
