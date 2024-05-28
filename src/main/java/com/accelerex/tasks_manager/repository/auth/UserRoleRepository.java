package com.accelerex.tasks_manager.repository.auth;


import com.accelerex.tasks_manager.model.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
