package org.meristem.oneapp.wealthservice.repositories;

import org.meristem.oneapp.wealthservice.models.InvestmentPlanSettings;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvestmentPlanSettingsRepository extends BaseRepository<InvestmentPlanSettings, Long> {

    @Query("SELECT * FROM investment_plan_settings WHERE investment_plan_id = :planId")
    Optional<InvestmentPlanSettings> findByInvestmentPlanId(@Param("planId") Long planId);
}