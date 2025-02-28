package com.oc.pay_my_buddy.service;

import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    private final String existingEmail = "test@example.com";
    private final String nonExistingEmail = "notfound@example.com";
    private final User mockUser = mock(User.class);

    @BeforeEach
    void setUp() {
        lenient().when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(mockUser));
        lenient().when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(existingEmail);
        assertNotNull(userDetails);
        verify(userRepository, times(1)).findByEmail(existingEmail);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailService.loadUserByUsername(nonExistingEmail));
        assertEquals("Utilisateur non trouvé : " + nonExistingEmail, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(nonExistingEmail);
    }
}
