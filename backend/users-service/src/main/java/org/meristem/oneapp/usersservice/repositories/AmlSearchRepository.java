package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.AmlSearch;

public interface AmlSearchRepository extends BaseRepository<AmlSearch, Long> {
    AmlSearch findAmlSearchByClientSearchId(String clientSearchId);
}
