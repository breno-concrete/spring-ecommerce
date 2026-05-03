package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.AddressRequestDTO;
import com.breno.marketplace_test.dtos.AddressResponseDTO;
import com.breno.marketplace_test.models.Address;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.AddressRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    public AddressResponseDTO saveAddress(AddressRequestDTO addressDTO) {
        User user = userRepository.findById(addressDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + addressDTO.userId() + " not found!"));

        Address address = new Address();
        address.setUser(user);
        address.setZipCode(addressDTO.zipCode());
        address.setStreet(addressDTO.street());
        address.setNumber(addressDTO.number());
        address.setComplement(addressDTO.complement());
        address.setNeighborhood(addressDTO.neighborhood());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());

        Address savedAddress = addressRepository.save(address);
        return convertToResponseDTO(savedAddress);
    }

    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));

        User user = userRepository.findById(addressDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + addressDTO.userId() + " not found!"));

        address.setUser(user);
        address.setZipCode(addressDTO.zipCode());
        address.setStreet(addressDTO.street());
        address.setNumber(addressDTO.number());
        address.setComplement(addressDTO.complement());
        address.setNeighborhood(addressDTO.neighborhood());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());

        Address updatedAddress = addressRepository.save(address);
        return convertToResponseDTO(updatedAddress);
    }

    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        addressRepository.delete(address);
    }

    private AddressResponseDTO convertToResponseDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getZipCode(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getUser().getId()
        );
    }
}

