package org.meristem.oneapp.usersservice.domains.annotations;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Custom annotation for modifying user-related queries in the database.
 * <p>
 * This annotation combines the following functionalities:
 * <ul>
 *   <li>Evicts the cache for the "users" cache with the specified key.</li>
 *   <li>Marks the annotated method as a modifying query, allowing it to perform update or delete operations.</li>
 *   <li>Ensures the annotated method is executed within a transactional context.</li>
 * </ul>
 * <strong>Always ensure the USER'S EMAIL is always the first argument to enforce the cache eviction</strong>
 * </p>
 *
 * Usage:
 * <pre>
 * &#64;UsersQueryModifier
 * &#64;Query("UPDATE users SET status = :userStatus WHERE email = :email")
 * void updateStatus(String email, Integer userStatus);
 * </pre>
 *
 * Annotations:
 * <ul>
 *   <li>&#64;CacheEvict: Clears the cache entry for the "users" cache using the first method argument as the key.</li>
 *   <li>&#64;Modifying: Indicates that the query modifies data in the database.</li>
 *   <li>&#64;Transactional: Ensures the query is executed within a transaction.</li>
 * </ul>
 *
 * Target: Methods only.
 * Retention: Runtime.
 *
 * @see org.springframework.cache.annotation.CacheEvict
 * @see org.springframework.data.jdbc.repository.query.Modifying
 * @see org.springframework.transaction.annotation.Transactional
 */
//@CacheEvict(cacheNames = "users", key = "#a0")
@Modifying
@Transactional
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UsersQueryModifier {
}
