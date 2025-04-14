package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.config.SecurityConfig;
import com.oc.pay_my_buddy.dto.Profil;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityConfig securityConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        lenient().when(securityConfig.passwordEncoder()).thenReturn(passwordEncoder);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void getAllUser_ShouldReturnUserList() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUser();
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldSaveAndReturnUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserProfil_ShouldUpdateUser_WhenValidProfil() {
        Profil profil = new Profil();
        profil.setUsername("newUser");
        profil.setEmail("new@example.com");
        profil.setPassword("newPass");
        profil.setConfirmPassword("newPass");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.updateUserProfil(1, profil);
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserProfil_ShouldReturnFalse_WhenPasswordsDoNotMatch() {
        Profil profil = new Profil();
        profil.setUsername("newUser");
        profil.setEmail("new@example.com");
        profil.setPassword("newPass");
        profil.setConfirmPassword("wrongPass");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        boolean result = userService.updateUserProfil(1, profil);
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldReturnTrue_WhenUserDeletedSuccessfully() {
        doNothing().when(userRepository).delete(user);

        boolean result = userService.deleteUser(user);
        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_ShouldReturnFalse_WhenExceptionOccurs() {
        doThrow(new RuntimeException("Error"))
                .when(userRepository).delete(user);

        boolean result = userService.deleteUser(user);
        assertFalse(result);
        verify(userRepository, times(1)).delete(user);
    }
}