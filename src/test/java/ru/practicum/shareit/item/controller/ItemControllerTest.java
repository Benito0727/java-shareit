package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.user.repository.DBUserRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.unit.TestUnits.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DBUserRepository userRepository;

    @Test
    void getItemSet() {

    }

    @Test
    void getItemById() {
    }

    @Test
    void addItem() throws Exception {
        userRepository.save(getUserEntity());

        RequestBuilder postRequest = post("/items")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(ITEM_1_JSON);

        RequestBuilder postRequestWithoutX_Sharer_User_Id = post("/items")
                .contentType(APPLICATION_JSON)
                .content(ITEM_2_JSON);

        mvc.perform(postRequestWithoutX_Sharer_User_Id)
                        .andExpect(status().is4xxClientError());

        mvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Простая дрель")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateItem() throws Exception {
        userRepository.save(getUserEntity());

        RequestBuilder postRequest = post("/items")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(ITEM_1_JSON);
        mvc.perform(postRequest);
        RequestBuilder patchRequest = patch("/items/1")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1)
                .content(JSON_TO_UPDATE_ITEM);

        mvc.perform(patchRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель+")))
                .andExpect(jsonPath("$.description", is("Аккумуляторная дрель")));
    }

    @Test
    void removeItem() {
    }

    @Test
    void getSearch() {
    }

    @Test
    void addComment() {
    }
}