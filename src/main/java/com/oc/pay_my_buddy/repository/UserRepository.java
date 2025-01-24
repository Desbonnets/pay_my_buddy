package com.oc.pay_my_buddy.repository;

import com.oc.pay_my_buddy.modele.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
