package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.unit.TestUnits.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void addRequest() throws Exception {

        mvc.perform(getUser1PostRequest());

        mvc.perform(getIR1PostRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.author.id", is(1)))
                .andExpect(jsonPath("$.description", is("Хотел бы воспользоваться щёткой для обуви")));

        RequestBuilder requestWithIncorrectUserId = post("/requests")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 999)
                .content(JSON_ITEM_REQUEST_1);

        mvc.perform(requestWithIncorrectUserId)
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getUserRequest() throws Exception {
        mvc.perform(getUser1PostRequest());
        RequestBuilder userIRRequestWithoutRequests = get("/requests")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(userIRRequestWithoutRequests)
                        .andExpect(status().isOk())
                        .andExpect(content().json("[]"));

        mvc.perform(getUser2PostRequest());
        mvc.perform(getIR1PostRequest());
        mvc.perform(getIR2PostRequest());

        RequestBuilder user1IRRequest = get("/requests")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(user1IRRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].author.id", is(1)));

        RequestBuilder user2IRRequest = get("/requests")
                .header("X-Sharer-User-Id", 2);

        mvc.perform(user2IRRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].author.id", is(2)));
    }

    @Test
    void getOtherUserRequests() throws Exception {
        mvc.perform(getUser1PostRequest());
        RequestBuilder userIRRequestWithoutRequests = get("/requests")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(userIRRequestWithoutRequests)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        mvc.perform(getUser2PostRequest());
        mvc.perform(getIR1PostRequest());
        mvc.perform(getIR2PostRequest());

        RequestBuilder user1IRAllRequest = get("/requests/all")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(user1IRAllRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(2)))
                .andExpect(jsonPath("$.[0].author.id", is(2)));

        RequestBuilder iRRequestWithIncorrectXSharerUserId = get("/requests/all")
                .header("X-Sharer-User-Id", 9999);
        mvc.perform(iRRequestWithIncorrectXSharerUserId)
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemRequest() throws Exception {
        mvc.perform(getUser1PostRequest());
        mvc.perform(getIR1PostRequest());

        RequestBuilder iRByIdRequest = get("/requests/1")
                .header("X-Sharer-User-Id", 1);
        mvc.perform(iRByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.author.id", is(1)));

        RequestBuilder iRWithIncorrectXSharerUserId = get("/requests/1")
                .header("X-Sharer-User-Id", 9999);
        mvc.perform(iRWithIncorrectXSharerUserId)
                .andExpect(status().isNotFound());
    }
}