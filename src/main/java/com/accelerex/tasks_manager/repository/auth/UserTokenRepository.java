package com.accelerex.tasks_manager.repository.auth;


import com.accelerex.tasks_manager.model.auth.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
