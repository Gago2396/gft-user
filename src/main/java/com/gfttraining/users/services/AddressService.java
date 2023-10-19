package com.gfttraining.users.services;

import com.gfttraining.users.models.Address;
import com.gfttraining.users.models.AddressRequest;
import com.gfttraining.users.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address parseAddress(AddressRequest addressRequest) {

        Address address = new Address();

        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setProvince(addressRequest.getProvince());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setCountry(addressRequest.getCountry());

        return address;
    }
}
