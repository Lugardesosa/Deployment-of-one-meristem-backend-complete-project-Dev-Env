package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserDocument;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserDocumentRepository extends BaseRepository<UserDocument, Long> {
    Optional<UserDocument> findByUserIdAndIdType(Long userId, String idType);
}
