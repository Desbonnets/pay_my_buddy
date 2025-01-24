package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(User user) {
        try {
            userRepository.delete(user);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
