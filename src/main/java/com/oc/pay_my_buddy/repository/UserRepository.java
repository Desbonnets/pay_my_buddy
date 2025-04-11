package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM user", nativeQuery = true)
    @Override
    java.util.List<User> findAll();

    @Query(value = "SELECT * FROM user WHERE id = :id", nativeQuery = true)
    @Override
    Optional<User> findById(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user (username, email, password) VALUES (:username, :email, :password)", nativeQuery = true)
    void insertUser(@Param("username") String username,
                    @Param("email") String email,
                    @Param("password") String password);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET username = :username, email = :email, password = :password WHERE id = :id", nativeQuery = true)
    void updateUser(@Param("id") int id,
                    @Param("username") String username,
                    @Param("email") String email,
                    @Param("password") String password);
}
