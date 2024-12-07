package com.laterna.xaxathonprime.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = """
            SELECT * FROM users u 
            WHERE (:search IS NULL 
                  OR CAST(u.firstname AS TEXT) ILIKE :searchPattern 
                  OR CAST(u.lastname AS TEXT) ILIKE :searchPattern 
                  OR CAST(u.patronymic AS TEXT) ILIKE :searchPattern)
            """,
            countQuery = """
            SELECT count(*) FROM users u 
            WHERE (:search IS NULL 
                  OR CAST(u.firstname AS TEXT) ILIKE :searchPattern 
                  OR CAST(u.lastname AS TEXT) ILIKE :searchPattern 
                  OR CAST(u.patronymic AS TEXT) ILIKE :searchPattern)
            """,
            nativeQuery = true)
    Page<User> findByNameContaining(@Param("search") String search,
                                    @Param("searchPattern") String searchPattern,
                                    Pageable pageable);

    @Query("""
            SELECT u FROM User u 
            WHERE (:search IS NULL OR 
                  LOWER(u.firstname) LIKE LOWER(:searchPattern) OR 
                  LOWER(u.lastname) LIKE LOWER(:searchPattern) OR 
                  LOWER(u.email) LIKE LOWER(:searchPattern)) AND 
                  u.role.name != :excludeRole
            """)
    Page<User> findByNameContainingExcludeRole(
            @Param("search") String search,
            @Param("searchPattern") String searchPattern,
            @Param("excludeRole") String excludeRole,
            Pageable pageable
    );

    @Query("""
            SELECT u FROM User u 
            WHERE (:search IS NULL OR 
                  LOWER(u.firstname) LIKE LOWER(:searchPattern) OR 
                  LOWER(u.lastname) LIKE LOWER(:searchPattern) OR 
                  LOWER(u.email) LIKE LOWER(:searchPattern)) AND 
                  u.role.name = :roleType
            """)
    Page<User> findByNameContainingAndRole(
            @Param("search") String search,
            @Param("searchPattern") String searchPattern,
            @Param("roleType") String roleType,
            Pageable pageable
    );
}
