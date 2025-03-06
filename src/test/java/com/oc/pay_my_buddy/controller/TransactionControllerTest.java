package com.oc.pay_my_buddy.controller;

import com.oc.pay_my_buddy.modele.Transaction;
import com.oc.pay_my_buddy.modele.User;
import com.oc.pay_my_buddy.service.TransactionService;
import com.oc.pay_my_buddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = TransactionController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "testuser@example.com")
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    private User mockUser;

    @BeforeEach
    public void setup() {
        transactionController = new TransactionController(transactionService, userService);
        mockUser = new User("testuser", "testuser@example.com", "password");
    }

    @Test
    void testGetAllTransactions_UserLoggedIn_ShouldReturnTransactionIndex() {
        when(userService.getUserByEmail(mockUser.getEmail())).thenReturn(mockUser);
        when(transactionService.getTransactionsBySenderId(mockUser)).thenReturn(Collections.emptyList());

        String viewName = transactionController.getAllTransactions(model, mockUser);

        verify(model).addAttribute(eq("users"), any());
        verify(model).addAttribute(eq("transaction"), any(Transaction.class));
        verify(model).addAttribute(eq("transactions"), any());
        assertEquals("/transaction/index", viewName);
    }

    @Test
    void testGetAllTransactions_UserNotLoggedIn_ShouldRedirectToLogin() {
        when(userService.getUserByEmail("test15@example.com")).thenReturn(null);

        String viewName = transactionController.getAllTransactions(model, mockUser);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testNewTransaction_ValidTransaction_ShouldRedirectToTransaction() {

        User user2 = new User("testuser2", "testuser2@example.com", "password2");
        Transaction transaction = new Transaction();
        transaction.setReceiver(user2);
        transaction.setAmount(100.00);
        transaction.setDescription("description");

        when(userService.getUserByEmail(mockUser.getEmail())).thenReturn(mockUser);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = transactionController.newTransaction(transaction, bindingResult, mockUser, model);

        verify(transactionService, times(1)).createTransaction(transaction);
        assertEquals("redirect:/transaction", viewName);
    }

    @Test
    void testNewTransaction_InvalidTransaction_ShouldReturnTransactionIndex() {
        Transaction transaction = new Transaction();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = transactionController.newTransaction(transaction, bindingResult, mockUser, model);

        assertEquals("transaction/index", viewName);
    }

    @Test
    void testNewTransaction_ExceptionThrown_ShouldReturnTransactionIndexWithError() {
        Transaction transaction = new Transaction();
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Erreur"))
                .when(transactionService).createTransaction(transaction);

        String viewName = transactionController.newTransaction(transaction, bindingResult, mockUser, model);

        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("transaction/index", viewName);
    }

//    @Test
//    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
//    public void testNewTransaction_Success() throws Exception {
//
//        User user2 = new User("testuser2", "testuser2@example.com", "password2");
//        Transaction transaction = new Transaction();
//        transaction.setReceiver(user2);
//        transaction.setAmount(100.00);
//        transaction.setDescription("description");
//
//        when(userService.getUserByEmail(mockUser.getEmail())).thenReturn(mockUser);
//        when(bindingResult.hasErrors()).thenReturn(false);
//
//        // Effectuer la requête MockMvc et vérifier le résultat
//        mockMvc.perform(post("/transaction")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .flashAttr("transaction", transaction))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/transaction"));
//
//        // Vérifier que la méthode du service a bien été appelée
//        verify(transactionService, times(1)).createTransaction(transaction);
//    }
}