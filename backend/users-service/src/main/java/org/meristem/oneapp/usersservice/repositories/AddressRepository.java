package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Address;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AddressRepository extends BaseRepository<Address, Long> {
    boolean existsByUserId(Long userId);
    Optional<Address> findByUserId(Long userId);
}
