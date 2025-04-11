package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.config.SecurityConfig;
import com.oc.pay_my_buddy.dto.Profil;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final Logger logger= LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User createUser(User user) {
        userRepository.insertUser(user.getUsername(), user.getEmail(), user.getPassword());
        return getUserByEmail(user.getEmail());
    }

    public User updateUser(User user) {
        userRepository.updateUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        return getUserById(user.getId());
    }

    /**
     * Modification du User à partir du formulaire Profil
     * @param id
     * @param profil
     * @return boolean
     */
    public boolean updateUserProfil(int id, Profil profil) {
        User user = this.getUserById(id);

        if (!(profil.getUsername().isEmpty() &&
                        profil.getPassword().isEmpty() &&
                        profil.getEmail().isEmpty() &&
                        profil.getConfirmPassword().isEmpty())
        ) {
            user.setUsername(profil.getUsername());
            user.setEmail(profil.getEmail());

            if (Objects.equals(profil.getPassword(), profil.getConfirmPassword())) {
                user.setPassword((this.securityConfig.passwordEncoder().encode(profil.getPassword())));
            }else {
                return false;
            }
        }else {
            return false;
        }
        updateUser(user);
        return true;
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
