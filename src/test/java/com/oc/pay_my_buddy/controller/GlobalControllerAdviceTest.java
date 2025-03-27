package com.oc.pay_my_buddy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class GlobalControllerAdviceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetCurrentPath() throws Exception {
        // Effectuer une requête GET sur un endpoint fictif
        mockMvc.perform(get("/user/new"))
                .andExpect(status().isOk()) // Vérifier que la requête renvoie un statut 200
                .andExpect(model().attribute("currentPath", "/user/new")); // Vérifier que le modèle contient "currentPath" avec l'URI
    }
}
