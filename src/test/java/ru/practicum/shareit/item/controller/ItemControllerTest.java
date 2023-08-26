package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.DBUserRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void getItemSet() throws Exception {
        userRepository.save(getUserEntity());
        mvc.perform(getItem1PostRequest());

        RequestBuilder itemSetRequest = get("/items")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(itemSetRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void getItemById() throws Exception {
        userRepository.save(getUserEntity());

        mvc.perform(getItem1PostRequest());

        RequestBuilder itemGetRequest = get("/items/1")
                .header("X-Sharer-User-Id", 1);
        mvc.perform(itemGetRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.owner", is(1)));

        RequestBuilder itemGetRequestWithIncorrectXSharerUserId = get("/items/1")
                .header("X-Sharer-User-Id", 9999);
        mvc.perform(itemGetRequestWithIncorrectXSharerUserId)
                .andExpect(status().isNotFound());
    }

    @Test
    void addItem() throws Exception {
        userRepository.save(getUserEntity());

        User userToIR = getUserEntity();
        userToIR.setId(2);
        userToIR.setEmail("other@mail.com");
        userRepository.save(userToIR);

        RequestBuilder postRequestWithoutXSharerUserId = post("/items")
                .contentType(APPLICATION_JSON)
                .content(ITEM_2_JSON);
        mvc.perform(postRequestWithoutXSharerUserId)
                        .andExpect(status().is4xxClientError());

        RequestBuilder postRequestWithIncorrectXSharerUserId = post("/items")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 9999)
                .content(ITEM_1_JSON);
        mvc.perform(postRequestWithIncorrectXSharerUserId)
                        .andExpect(status().isNotFound());

        mvc.perform(getItem1PostRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Простая дрель")))
                .andExpect(jsonPath("$.id", is(1)));

        mvc.perform(getIR1PostRequest())
                .andExpect(status().isOk());

        RequestBuilder postItemToIR = post("/items")
                .contentType(APPLICATION_JSON)
                .header("X-Sharer-User-Id", 2)
                .content(ITEM_TO_ITEM_REQUEST_JSON);

        mvc.perform(postItemToIR)
                .andExpect(status().isOk());

        RequestBuilder getIRRequest = get("/requests/1")
                .header("X-Sharer-User-Id", 1);
        mvc.perform(getIRRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.items.[0].id", is(2)));
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
    void removeItem() throws Exception {
        userRepository.save(getUserEntity());
        mvc.perform(getItem1PostRequest());
        User user = getUserEntity();
        user.setId(2);
        user.setEmail("other@mail.com");
        userRepository.save(user);
        RequestBuilder itemRemoveRequestWithIncorrectXSharerUserId = delete("/items/1")
                .header("X-Sharer-User-Id", 9999);

        RequestBuilder itemRemoveRequest = delete("/items/1")
                .header("X-Sharer-User-Id", 1);

        RequestBuilder itemRemoveRequestFromNotOwner = delete("/items/1")
                .header("X-Sharer-User-Id", 2);

        mvc.perform(itemRemoveRequestFromNotOwner)
                        .andExpect(status().isBadRequest());

        mvc.perform(itemRemoveRequestWithIncorrectXSharerUserId)
                        .andExpect(status().isNotFound());

        mvc.perform(itemRemoveRequest)
                .andExpect(status().isOk());


    }

    @Test
    void getSearch() throws Exception {
        userRepository.save(getUserEntity());
        mvc.perform(getItem1PostRequest());

        RequestBuilder searchItemRequest = get("/items/search?text=Дрель")
                .header("X-Sharer-User-Id", 1);

        RequestBuilder searchItemWithIncorrectXSharerUserIdRequest = get("/items/search?text=Дрель")
                .header("X-Sharer-User-Id", 9999);

        mvc.perform(searchItemWithIncorrectXSharerUserIdRequest)
                        .andExpect(status().isNotFound());

        mvc.perform(searchItemRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Дрель")));
    }

    @Test
    void addComment() throws Exception {
        userRepository.save(getUserEntity());
        User userToBooking = getUserEntity();
        userToBooking.setId(2);
        userToBooking.setEmail("booking@user.com");
        userRepository.save(userToBooking);
        mvc.perform(getItem1PostRequest());

        RequestBuilder bookingPostRequest = post("/bookings")
                .header("X-Sharer-User-Id", 2)
                .contentType(APPLICATION_JSON)
                .content(BOOKING_1_JSON);

        mvc.perform(bookingPostRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        RequestBuilder setApprovedRequest = patch("/bookings/1?approved=true")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(setApprovedRequest)
                .andExpect(status().isOk());

        RequestBuilder commentPostRequest = post("/items/1/comment")
                .header("X-Sharer-User-Id", 2)
                .contentType(APPLICATION_JSON)
                .content(COMMENT_1_JSON);

        mvc.perform(commentPostRequest)
                .andExpect(status().isBadRequest());

        RequestBuilder commentPostWithFakeUserRequest = post("/items/1/comment")
                .header("X-Sharer-User-Id", 9999)
                .contentType(APPLICATION_JSON)
                .content(COMMENT_1_JSON);

        mvc.perform(commentPostWithFakeUserRequest)
                .andExpect(status().isNotFound());

        RequestBuilder commentPostToFakeItemRequest = post("/items/9999/comment")
                .header("X-Sharer-User-Id", 1)
                .contentType(APPLICATION_JSON)
                .content(COMMENT_1_JSON);

        mvc.perform(commentPostToFakeItemRequest)
                .andExpect(status().isNotFound());

        RequestBuilder itemSetRequest = get("/items")
                .header("X-Sharer-User-Id", 1);

        mvc.perform(itemSetRequest);

    }
}