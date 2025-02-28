package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.repository.TransactionRepository;
import com.oc.pay_my_buddy.service.TransactionService;
import com.oc.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @MockBean
    private TransactionService transactionService;

    private TransactionController transactionController;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserDetails userDetails; // Mock de UserDetails

    @BeforeEach
    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testNewTransaction_Success() throws Exception {
        // Créer un utilisateur fictif
        User user = new User("testuser", "testuser@example.com", "password");

        // Simuler la réponse du service userService
        when(userService.getUserByEmail("testuser@example.com")).thenReturn(user);

        // Utiliser un mock de UserDetails pour simuler un utilisateur authentifié
        when(userDetails.getUsername()).thenReturn("testuser@example.com");

        // Créer une transaction fictive
        Transaction transaction = new Transaction();
        transaction.setSender(user);

        // Effectuer la requête MockMvc et vérifier le résultat
        mockMvc.perform(post("/transaction")
                        .flashAttr("transaction", transaction))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaction"));

        // Vérifier que la méthode de service a bien été appelée
        verify(transactionService, times(1)).createTransaction(transaction);
    }
}
