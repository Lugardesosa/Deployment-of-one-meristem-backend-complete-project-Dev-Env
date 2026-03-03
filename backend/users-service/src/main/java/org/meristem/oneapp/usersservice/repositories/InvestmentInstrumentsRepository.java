package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.dtos.sql.UserInvestmentOptionsResultSetExtractor;
import org.meristem.oneapp.usersservice.dtos.sql.UserResponseResultSetExtractor;
import org.meristem.oneapp.usersservice.models.InvestmentInstruments;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface InvestmentInstrumentsRepository extends BaseRepository<InvestmentInstruments, Long> {

    @Cacheable(value = "investment_instruments", key = "#a0", unless = "#result == null")
//    @Query(value = """
//
//            SELECT ia.*, ii.*
//            FROM user_instrument ia
//            JOIN investment_instruments ii ON ii.id = ia.instrument_id
//            WHERE ia.user_id = :id AND ii.status = 1 ORDER BY ii.id
//            """)

    @Query(value = "SELECT ii.code, ii.id AS iiid, ii.name, ia.data_sharing_allowed, ia.accessed, " +
            "io.id AS o_iiid, io.name AS o_name, ioa.accessed AS o_accessed FROM user_instrument ia ON ia.user_id = u.id LEFT JOIN investment_instruments ii ON ii.id = ia.instrument_id " +
            " LEFT JOIN investment_options io ON io.investment_id = ii.id LEFT JOIN investment_options_accessed ioa ON ioa.option_id = io.id " +
            "WHERE ii.status = 1 ORDER BY ii.id", resultSetExtractorClass = UserInvestmentOptionsResultSetExtractor.class)
    UsersResponse findUserInstrumentsById(Long id);


    @Cacheable(value = "investment_options", key = "#a0", unless = "#result == null")
    @Query(value = """
            
            SELECT io.*, ioa.accessed, ii.code
            FROM investment_options io
            LEFT JOIN investment_instruments ii ON ii.id = io.investment_id
            LEFT JOIN investment_options_accessed ioa ON ioa.option_id = io.id
            WHERE io.investment_id IN (:ids)
            """, resultSetExtractorClass = UserInvestmentOptionsResultSetExtractor.class)
    List<UsersResponse.UserOptionResponse> findUserInstrumentsOptionsById(List<Long> ids);

    List<UsersResponse.UserInstrumentResponse> findUserInstrumentsByIdOrderById(Long id);
}
