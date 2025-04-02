package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.FeatureRequirement;

import java.util.List;

public interface FeatureRequirementRepository extends BaseRepository<FeatureRequirement, Long> {
    List<FeatureRequirement> findAllByFeatureId(@NotNull(message = "featureId cannot be null") Long featureId);
}
