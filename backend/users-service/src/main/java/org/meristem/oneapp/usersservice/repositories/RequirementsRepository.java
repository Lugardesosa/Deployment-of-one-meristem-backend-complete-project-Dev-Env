package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.dtos.sql.InvestmentReqIdReqName;
import org.meristem.oneapp.usersservice.models.InvestmentRequirement;
import org.meristem.oneapp.usersservice.models.Requirements;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RequirementsRepository extends BaseRepository<Requirements, Long> {

    @Query("SELECT id FROM requirements WHERE requirement_name = :requirementName")
    Long findIdByRequirementName(String requirementName);

    @Query("SELECT id FROM requirements WHERE status = :status ")
    Iterable<Long> findAllByStatus(Integer status);


    @Query("SELECT ir.id, r.requirement_name FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id WHERE r.status = :status AND ir.investment_id = :investmentId ")
    Iterable<InvestmentReqIdReqName> findAllProductsRequirementByStatus(Integer status, Long investmentId);

    @Query("SELECT ir.id FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id WHERE r.status = :status AND ir.investment_id = :investmentId ")
    Optional<Long> findOneProductsRequirementByStatus(Integer status, Long investmentId);

    Optional<Requirements> findByIdAndStatus(Long id, Integer status);

    Requirements findByRequirementNameAndStatus(String requirementName, Integer status);

    Requirements findRequirementsByRequirementName(String requirementName);

    @Query("""
                    SELECT ir.* FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id 
                    LEFT JOIN investment_instruments ii ON ii.id = ir.investment_id
                    WHERE r.requirement_name = :requirementName AND ii.id = :productId
            """)
    InvestmentRequirement findInvestmentRequirementsByRequirementName(String requirementName, Long productId);

    @Query("""
                    SELECT ir.* FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id 
                    LEFT JOIN investment_instruments ii ON ii.id = ir.investment_id
                    WHERE r.requirement_name = :requirementName AND r.status = :status AND ii.id = :productId
            """)
    Optional<InvestmentRequirement> findInvestmentRequirementsByRequirementName(String requirementName, Integer status, Long productId);

    @Query("""
                    SELECT ir.id FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id 
                    LEFT JOIN investment_instruments ii ON ii.id = ir.investment_id
                    WHERE r.requirement_name = :requirementName AND ii.id != :investmentId
            """)
    List<Long> findAllInvestmentRequirementIdByRequirementNameAndNotInvestmentId(String requirementName, Long investmentId);

    @Query("""
                    SELECT ii.id FROM investment_requirement ir LEFT JOIN requirements r ON r.id = ir.requirement_id 
                    LEFT JOIN investment_instruments ii ON ii.id = ir.investment_id
                    WHERE r.requirement_name = :requirementName AND ii.id != :investmentId
            """)
    List<Long> findAllInvestmentInstrumentIdByRequirementName(String requirementName, Long investmentId);
}
