package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.responses.ExistingInstrumentResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.dtos.sql.UserInvestmentOptionsResultSetExtractor;
import org.meristem.oneapp.usersservice.models.InvestmentInstruments;
import org.meristem.oneapp.usersservice.models.InvestmentOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional(readOnly = true)
public interface InvestmentInstrumentsRepository extends BaseRepository<InvestmentInstruments, Long> {

    @Cacheable(value = AppConstants.INVESTMENT_INSTRUMENT_CACHE_NAME, key = "#a0", unless = "#result == null")
    @Query(value = "SELECT ii.code, ii.id, ii.name, ia.accessed, ia.kyc_completed " +
            " FROM user_instrument ia LEFT JOIN investment_instruments ii ON ii.id = ia.instrument_id " +
            " WHERE ii.status = 1 AND ia.user_id = :id ORDER BY ii.id ")
    List<UsersResponse.UserInstrumentResponse> findUserInstrumentsById(Long id);

    @Cacheable(value = AppConstants.INVESTMENT_OPTIONS_CACHE_NAME, key = "#a0", unless = "#result == null")
    @Query(value = "SELECT ii.code, io.id AS o_iiid, io.name AS o_name, ioa.accessed AS o_accessed FROM investment_options io LEFT JOIN investment_instruments ii ON ii.id = io.investment_id " +
            " LEFT JOIN investment_options_accessed ioa ON ioa.option_id = io.id " +
            "WHERE ii.status = 1 AND ioa.user_id = :id ORDER BY ii.id", resultSetExtractorClass = UserInvestmentOptionsResultSetExtractor.class)
    Map<String, Set<UsersResponse.UserOptionResponse>> findUserInstrumentOptionsById(Long id);

    List<UsersResponse.UserInstrumentResponse> findUserInstrumentsByIdOrderById(Long id);

    @Query("""
        SELECT investment_id FROM investment_instruments WHERE id = :investmentRequirementId
    """)
    Long findInvestmentIdById(Long investmentRequirementId);

    @Query("SELECT io.* FROM investment_options io WHERE io.investment_id = :id")
    List<InvestmentOptions> findInvestmentOptionsByInvestmentId(Long id);

    List<ExistingInstrumentResponse> findInvestmentInstrumentsByCodeIn(Collection<String> codes);

    @Query(value = "SELECT ii.code, ii.id, ii.name FROM investment_instruments ii WHERE ii.status = 1 ORDER BY ii.id ")
    List<UsersResponse.UserInstrumentResponse> findAllUserInstrumentsBy();

    @Query(value = "SELECT ii.code, io.id AS o_iiid, io.name AS o_name, ioa.accessed AS o_accessed FROM investment_options io LEFT JOIN investment_instruments ii ON ii.id = io.investment_id " +
            " LEFT JOIN investment_options_accessed ioa ON ioa.option_id = io.id " +
            "WHERE ii.status = 1 ORDER BY ii.id", resultSetExtractorClass = UserInvestmentOptionsResultSetExtractor.class)
    Map<String, Set<UsersResponse.UserOptionResponse>> findAllUserInstrumentOptions();
}
