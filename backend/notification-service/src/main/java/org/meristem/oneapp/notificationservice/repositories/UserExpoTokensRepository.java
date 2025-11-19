package org.meristem.oneapp.notificationservice.repositories;

import org.meristem.oneapp.notificationservice.models.UserExpoTokens;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserExpoTokensRepository extends BaseRepository<UserExpoTokens, Long> {
    @Query("SELECT expo_token FROM user_expo_tokens")
    List<String> findAllExpoTokens();

    @Query("SELECT expo_token FROM user_expo_tokens WHERE user_id = :userId")
    List<String> findAllExpoTokensByUserId(Long userId);

    void deleteUserExpoTokensByExpoToken(String expoToken);

    void deleteUserExpoTokensByExpoTokenIn(Collection<String> expoTokens);

    Optional<UserExpoTokens> findOneByDeviceId(String deviceId);

    boolean existsByExpoToken(String expoToken);
}
