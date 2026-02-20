package org.meristem.oneapp.usersservice.repositories;


import org.meristem.oneapp.usersservice.models.Countries;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.Optional;

public interface CountriesRepositories extends BaseRepository<Countries, Long> {
    Optional<Countries> findCountriesByCodeLongOrCodeShortOrNameIgnoreCase(String codeLong, String codeShort, String name);

    Countries getCountriesById(Long id);

    @Query("SELECT code_long FROM countries WHERE id = :countryId")
    String getCodeLongById(Long countryId);
}
