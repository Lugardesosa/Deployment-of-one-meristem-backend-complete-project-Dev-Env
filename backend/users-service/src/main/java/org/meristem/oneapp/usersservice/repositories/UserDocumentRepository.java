package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserDocument;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserDocumentRepository extends BaseRepository<UserDocument, Long> {
}
