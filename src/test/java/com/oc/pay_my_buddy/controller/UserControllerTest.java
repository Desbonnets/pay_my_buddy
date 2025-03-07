package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.config.SecurityConfig;
import com.oc.pay_my_buddy.dto.Profil;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.CustomUserDetailService;
import com.oc.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityConfig securityConfig;

    @InjectMocks
    private UserController userController;

    @MockBean
    private CustomUserDetailService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, securityConfig);
        testUser = new User("TestUser", "test@example.com", "password123");
//        userService.createUser(testUser);
    }

//    @Test
//    void testLoadUserByUsername() {
//        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
//        assertNotNull(userDetails);
//        assertEquals("test@example.com", userDetails.getUsername());
//    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testGetAllUsers_ShouldReturnUserIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testShowUserForm_ShouldReturnNewUserForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/new"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testCreateUser_ValidUser_ShouldRedirect() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/new")
                        .param("username", "TestUser")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testShowRelationForm_ShouldReturnAddRelationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/add_relation"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add_relation"))
                .andExpect(model().attributeExists("relation"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testAddRelation_ValidRelation_ShouldRedirect() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(testUser);
        User friend = new User("FriendUser", "friend@example.com", "password123");
        when(userService.getUserByEmail("friend@example.com")).thenReturn(friend);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add_relation")
                        .param("email", "friend@example.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaction"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testShowUserProfile_ShouldReturnProfilePage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("profil"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testEditUserForm_ShouldReturnEditPage() throws Exception {
        when(userService.getUserById(1)).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/edit"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("profil"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testUpdateUser_ValidProfil_ShouldRedirect() throws Exception {
        Profil profil = new Profil();
        profil.setEmail("profil@example.com");
        profil.setPassword("password123");
        profil.setUsername("profil@example.com");
        profil.setConfirmPassword("password123");

        when(userService.updateUserProfil(1, profil)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/edit/1")
                        .param("username", "UpdatedUser")
                        .param("email", "updated@example.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));
    }
}
