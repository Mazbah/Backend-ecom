package com.mazbah.ecomd.repository;

import com.mazbah.ecomd.entity.AuthToken;
import com.mazbah.ecomd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    AuthToken findTokenByUser(User user);
    AuthToken findTokenByToken(String token);
}
