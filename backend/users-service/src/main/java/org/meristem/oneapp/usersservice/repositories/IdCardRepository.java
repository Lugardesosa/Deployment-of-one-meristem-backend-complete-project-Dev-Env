package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.IdCard;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface IdCardRepository extends BaseRepository<IdCard, Long> {
    boolean existsByIdValue(String idValue);

    Optional<IdCard> findByIdCardTypeAndIdValue(String idCardType, String idValue);

    @Query("SELECT id_value FROM id_card WHERE user_id = :loggedInUserId AND id_card_type = :idType")
    String findIdCardValueByUserId(Long loggedInUserId, String idType);

    boolean existsByIdValueAndIdCardType(String idValue, String idCardType);

    boolean existsByIdValueHashed(String idValueHashed);

    boolean existsByIdValueHashedAndIdCardType(String idValueHashed, String idCardType);

    Optional<IdCard> findByIdCardTypeAndIdValueHashed(String idCardType, String idValueHashed);

    boolean existsByIdValueHashedInAndIdCardType(List<String> bvns, String name);

    boolean existsByIdValueHashedAndIdCardTypeAndUserIdNot(String idValueHashed, String idCardType, Long userId);

    Optional<IdCard> findByIdCardTypeAndIdValueHashedAndUserId(String name, String ninValueHashed, Long id);

    Optional<IdCard> findByIdValueHashedAndIdCardType(String idValueHashed, String idCardType);

    @Query("SELECT u.* FROM users u LEFT JOIN id_card idc ON idc.user_id = u.id WHERE idc.id_value_hashed = :decrypt AND idc.id_card_type = :idType ")
    Users findUsersByIdCardNumberHashedAndType(String decrypt, String idType);
}
