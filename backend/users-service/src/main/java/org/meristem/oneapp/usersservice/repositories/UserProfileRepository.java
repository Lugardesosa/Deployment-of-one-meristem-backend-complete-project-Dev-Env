package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserProfile;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserProfileRepository extends BaseRepository<UserProfile, Long> {
}
