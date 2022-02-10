package com.example.postapp.repository;

import com.example.postapp.service.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    @Query("select t from ConfirmationToken t where t.token = :token")
    Optional<ConfirmationToken> findConfirmationTokenByToken (@Param("token") String token);

    @Transactional
    @Modifying
    @Query("update ConfirmationToken t set t.confirmedAt = :confirmedAt where t.token = :token")
    void setConfirmedAt(@Param("token") String token, @Param("confirmedAt") LocalDateTime confirmedAt);

}
