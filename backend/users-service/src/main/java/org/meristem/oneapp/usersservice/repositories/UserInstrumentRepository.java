package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.annotations.UsersQueryModifier;
import org.meristem.oneapp.usersservice.models.UserInstrument;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface UserInstrumentRepository extends BaseRepository<UserInstrument, Long> {

    @UsersQueryModifier
    @Query("UPDATE user_instrument SET kyc_completed = :b WHERE user_id = :userId AND instrument_id = :investmentId ")
    void updateUserInstrumentKycStatus(Long userId, boolean b, Long investmentId);

    @Query("UPDATE user_instrument SET data_sharing_allowed = :b WHERE user_id = :user_id AND instrument_id = :investmentId ")
    void updateUserInstrumentDataSharingAllowed(Long userId, boolean b, Long investmentId);

    @UsersQueryModifier
    @Query("UPDATE user_instrument SET kyc_completed = :b WHERE user_id = :userId AND instrument_id IN (:investmentId) ")
    void updateAllUserInstrumentKycStatus(Long userId, boolean b, List<Long> investmentId);

    UserInstrument findUserInstrumentByInstrumentId(Long instrumentId);
}
