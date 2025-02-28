package com.oc.pay_my_buddy.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomErrorControllerTest {

    @InjectMocks
    private CustomErrorController customErrorController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    // Initialisation des mocks avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetErrorWithValidErrorStatus() {
        // Arrange
        int errorCode = 500;
        String errorMessage = "Internal Server Error";
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(errorCode);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn(errorMessage);

        // Act
        String viewName = customErrorController.getError(request, model);

        // Assert
        verify(model).addAttribute("errorCode", errorCode);
        verify(model).addAttribute("errorMessage", errorMessage);
        assertEquals("default/error", viewName);
    }

    @Test
    void testGetErrorWithNullErrorStatus() {
        // Arrange
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        // Act
        String viewName = customErrorController.getError(request, model);

        // Assert
        verify(model, times(0)).addAttribute(anyString(), any());
        assertEquals("default/error", viewName);
    }
}
