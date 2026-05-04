package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

