package org.meristem.oneapp.wealthservice.repositories;

import org.meristem.oneapp.wealthservice.models.InvestmentPlans;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestmentPlansRepository extends BaseRepository<InvestmentPlans, Long> {

    @Query("SELECT * FROM investment_plans WHERE investment_product_id = :productId ORDER BY position ASC")
    List<InvestmentPlans> findByInvestmentProductId(@Param("productId") Long productId);
}