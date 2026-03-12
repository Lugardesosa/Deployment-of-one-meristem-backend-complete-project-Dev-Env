package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.annotations.UsersQueryModifier;
import org.meristem.oneapp.usersservice.models.UserInstrument;
import org.springframework.data.jdbc.repository.query.Query;

public interface UserInstrumentRepository extends BaseRepository<UserInstrument, Long> {

    @UsersQueryModifier
    @Query("UPDATE user_instrument SET kyc_completed = :b WHERE user_id = :userId AND instrument_id = :investmentId ")
    void updateUserInstrumentKycStatus(Long userId, boolean b, Long investmentId);

    @Query("UPDATE user_instrument SET data_sharing_allowed = :b WHERE user_id = :user_id AND instrument_id = :investmentId ")
    void updateUserInstrumentDataSharingAllowed(Long userId, boolean b, Long investmentId);
}
