package com.accelerex.tasks_manager.repository.auth;


import com.accelerex.tasks_manager.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findByEmail(String email);

}