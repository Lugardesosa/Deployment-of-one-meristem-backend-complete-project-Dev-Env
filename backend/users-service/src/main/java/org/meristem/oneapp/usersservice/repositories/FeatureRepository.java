package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.Feature;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//public interface UsersRepository extends BaseRepository<Users, Long> {

@Transactional(readOnly = true)
public interface FeatureRepository extends BaseRepository<Feature, Long> {

    /**
     * <strong>WARNING</strong>: Only use on tables that contain less than 200 data
     */
    @Query("SELECT id FROM feature")
    List<Long> findAllIds();

    boolean existsByIdAndStatus(@NotNull(message = "id cannot be null") Long id, @NotNull(message = "Cannot be null") Integer status);
}
