package com.example.postapp.repository;

import com.example.postapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.email = :email")
    Optional<UserEntity> findUserByEmail (@Param("email") String email);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.status = 'ACTIVE' where u.email = :email")
    void enableUser(@Param("email") String email);

}
