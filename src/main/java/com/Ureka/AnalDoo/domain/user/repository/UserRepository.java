package com.Ureka.AnalDoo.domain.user.repository;

import com.Ureka.AnalDoo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
