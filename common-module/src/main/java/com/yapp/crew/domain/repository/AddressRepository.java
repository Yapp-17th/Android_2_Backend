package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findAll();

  Address save(Address address);
}
