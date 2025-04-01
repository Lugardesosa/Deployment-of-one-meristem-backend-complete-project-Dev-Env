package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserFeature;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserFeatureRepository extends BaseRepository<UserFeature, Long> {
}
