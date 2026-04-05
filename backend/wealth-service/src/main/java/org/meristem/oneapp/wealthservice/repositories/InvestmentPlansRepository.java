package org.meristem.oneapp.wealthservice.repositories;

import org.meristem.oneapp.wealthservice.models.InvestmentPlans;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvestmentPlansRepository extends BaseRepository<InvestmentPlans, Long> {

    @Query("SELECT * FROM investment_plans WHERE investment_product_id = :productId ORDER BY position ASC")
    List<InvestmentPlans> findByInvestmentProductId(@Param("productId") Long productId);

    @Query("SELECT * FROM investment_plans WHERE uuid = :uuid")
    Optional<InvestmentPlans> findByUuid(@Param("uuid") String uuid);

    @Query("SELECT * FROM investment_plans WHERE core_product_id = :coreProductId LIMIT 1")
    Optional<InvestmentPlans> findByCoreProductId(@Param("coreProductId") String coreProductId);
}