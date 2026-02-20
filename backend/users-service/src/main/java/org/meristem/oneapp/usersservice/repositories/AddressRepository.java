package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AddressRepository extends BaseRepository<Address, Long> {
    boolean existsByUserId(Long userId);
    Optional<Address> findByUserId(Long userId);

    Optional<Address> findByUserIdAndVerificationMethod(Long userId, Integer verificationMethod);

    Address findAddressByUserIdAndVerificationMethod(Long userId, Integer verificationMethod);
}
