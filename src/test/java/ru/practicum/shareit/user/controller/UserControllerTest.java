package ru.practicum.shareit.user.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.RequestBuilder;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.unit.TestUnits.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void addUser() throws Exception {
        RequestBuilder requestBuilder = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.name").value(is("user")))
                .andExpect(jsonPath("$.email").value(is("user@user.com")));
    }

    @Test
    void updateUser() throws Exception {
        RequestBuilder postRequest = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);
        RequestBuilder patchRequest = patch("/users/1")
                .contentType(APPLICATION_JSON)
                .content(JSON_TO_UPDATE_USER);

        mvc.perform(postRequest);

        mvc.perform(patchRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.name").value(is("update")))
                .andExpect(jsonPath("$.email").value(is("update@user.com")));
    }

    @Test
    void getUserById() throws Exception {
        RequestBuilder postRequest = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);
        RequestBuilder getRequest = get("/users/1");

        mvc.perform(postRequest);

        mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.name").value(is("user")))
                .andExpect(jsonPath("$.email").value(is("user@user.com")));
    }

    @Test
    void removeUser() throws Exception {
        RequestBuilder postRequest = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);
        RequestBuilder getRequest = get("/users/1");
        RequestBuilder deleteRequest = delete("/users/1");
        mvc.perform(postRequest);
        mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.name").value(is("user")))
                .andExpect(jsonPath("$.email").value(is("user@user.com")));

        mvc.perform(deleteRequest)
                .andExpect(status().isOk());

        mvc.perform(getRequest)
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllUsers() throws Exception {
        RequestBuilder postRequest1 = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_1_JSON);
        RequestBuilder postRequest2 = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_2_JSON);
        RequestBuilder postRequest3 = post("/users")
                .contentType(APPLICATION_JSON)
                .content(USER_3_JSON);

        mvc.perform(postRequest1);
        mvc.perform(postRequest2);
        mvc.perform(postRequest3);

        RequestBuilder getRequest = get("/users");

        mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

    }
}