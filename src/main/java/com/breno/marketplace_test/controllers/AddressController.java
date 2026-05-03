package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.AddressRequestDTO;
import com.breno.marketplace_test.dtos.AddressResponseDTO;
import com.breno.marketplace_test.models.Address;
import com.breno.marketplace_test.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "List all addresses", description = "Returns a list of all addresses")
    public List<Address> getAddresses() {
        return addressService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create Address", description = "Creates a new address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody AddressRequestDTO address) {
        return ResponseEntity.ok(addressService.saveAddress(address));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Address", description = "Returns an address by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public Address getAddressById(@PathVariable Long id) {
        return addressService.findAddressById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Address", description = "Updates an address by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long id, @RequestBody AddressRequestDTO address) {
        return ResponseEntity.ok(addressService.updateAddress(id, address));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Address", description = "Deletes an address by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address deleted"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully!");
    }
}

