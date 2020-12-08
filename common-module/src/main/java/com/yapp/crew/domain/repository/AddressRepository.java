package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findAll();

	Address save(Address address);

	Optional<Address> findAddressById(long id);
}
