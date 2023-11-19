package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    void deleteUserById(int id);

    Optional<User> findUserById(int id);

    Optional<User> findByEmail(String email);
}
