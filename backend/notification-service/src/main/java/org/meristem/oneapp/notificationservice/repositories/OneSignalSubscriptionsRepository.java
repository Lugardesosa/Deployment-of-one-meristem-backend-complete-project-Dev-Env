package org.meristem.oneapp.notificationservice.repositories;

import org.meristem.oneapp.notificationservice.models.OneSignalSubscriptions;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OneSignalSubscriptionsRepository extends BaseRepository<OneSignalSubscriptions, Long> {
    Optional<OneSignalSubscriptions> findOneByDeviceId(String deviceId);

    @Query("SELECT subscription_id FROM one_signal_subscriptions")
    List<String> findAllSubscriptionIds();

    @Query("SELECT subscription_id FROM one_signal_subscriptions WHERE user_id = :userId")
    Collection<String> findAllSubscriptionIdsByUserId(Long userId);
}
