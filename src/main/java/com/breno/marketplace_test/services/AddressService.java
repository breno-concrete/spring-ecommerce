package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.AddressRequestDTO;
import com.breno.marketplace_test.dtos.AddressResponseDTO;
import com.breno.marketplace_test.mappers.AddressMapper;
import com.breno.marketplace_test.models.Address;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.AddressRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional; // USE ESTA
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> findAll() {
        List<Address> addresses = addressRepository.findAll();
        return addressMapper.toDTOList(addresses);
    }

    @Transactional(readOnly = true)
    public AddressResponseDTO findAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        return addressMapper.toDTO(address);
    }

    @Transactional
    public AddressResponseDTO saveAddress(AddressRequestDTO addressDTO) {
        log.info("Salvando novo endereço para o usuário: {}", addressDTO.userId());
        User user = userRepository.findById(addressDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao tentar salvar endereço", addressDTO.userId());
                    return new IllegalStateException("User " + addressDTO.userId() + " not found!");
                });

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
        log.info("Endereço salvo com sucesso. ID: {}, CEP: {}", savedAddress.getId(), savedAddress.getZipCode());
        return convertToResponseDTO(savedAddress);
    }
    @Transactional
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO addressDTO) {
        log.info("Atualizando endereço com ID: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Endereço com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        User user = userRepository.findById(addressDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao tentar atualizar endereço", addressDTO.userId());
                    return new IllegalStateException("User " + addressDTO.userId() + " not found!");
                });

        address.setUser(user);
        address.setZipCode(addressDTO.zipCode());
        address.setStreet(addressDTO.street());
        address.setNumber(addressDTO.number());
        address.setComplement(addressDTO.complement());
        address.setNeighborhood(addressDTO.neighborhood());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());

        Address updatedAddress = addressRepository.save(address);
        log.info("Endereço com ID {} atualizado com sucesso", id);
        return convertToResponseDTO(updatedAddress);
    }
    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deletando endereço com ID: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Endereço com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        addressRepository.delete(address);
        log.info("Endereço com ID {} deletado com sucesso", id);
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

