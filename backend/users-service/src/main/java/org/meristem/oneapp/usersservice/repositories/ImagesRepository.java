package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Images;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ImagesRepository extends BaseRepository<Images, Long> {
    boolean existsByImageKey(String url);

    Optional<Images> findByImageKey(String imageKey);

    List<Images> findAllByImageType(Integer imageType);

    Optional<Images> findByImageKeyAndImageType(String imageKey, Integer imageType);

    Optional<Images> findByImageTypeAndUserId(Integer imageType, Long userId);
}
