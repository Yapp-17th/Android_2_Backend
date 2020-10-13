package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findAll();

  Address save(Address category);
}
