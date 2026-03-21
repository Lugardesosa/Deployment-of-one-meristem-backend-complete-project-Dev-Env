package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Files;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface FilesRepository extends BaseRepository<Files, Long> {
    boolean existsByFileKey(String url);

    Optional<Files> findByFileKey(String fileKey);

    List<Files> findAllByFileType(Integer fileType);

    Optional<Files> findByFileKeyAndFileType(String fileKey, Integer fileType);

    Optional<Files> findByFileTypeAndUserId(Integer fileType, Long userId);

    Files findFilesByFileKey(String fileKey);
}
