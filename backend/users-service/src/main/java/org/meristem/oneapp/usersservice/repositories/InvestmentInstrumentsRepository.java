package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.InvestmentInstruments;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface InvestmentInstrumentsRepository extends BaseRepository<InvestmentInstruments, Long> {
}
