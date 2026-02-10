package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.IdCard;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

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
}
