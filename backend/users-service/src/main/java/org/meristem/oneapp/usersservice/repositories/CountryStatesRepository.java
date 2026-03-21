package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.CountryStates;

public interface CountryStatesRepository extends BaseRepository<CountryStates, Long> {
    CountryStates findCountryStatesByCodeOrNameIgnoreCase(String code, String name);
}
