package org.example.usercrud.controller;

import org.controller.UserController;
import org.dao.UserDTO;
import org.junit.jupiter.api.Test;
import org.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldGetAllUsers() throws Exception {
        UserDTO userDTO = createTestUserDTO();
        given(userService.findAll()).willReturn(Collections.singletonList(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        UserDTO userDTO = createTestUserDTO();
        given(userService.findById(anyLong())).willReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO userDTO = createTestUserDTO();
        given(userService.create(any(UserDTO.class))).willReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\",\"email\":\"test@example.com\",\"age\":30}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDTO userDTO = createTestUserDTO();
        given(userService.update(anyLong(), any(UserDTO.class))).willReturn(userDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated User\",\"email\":\"updated@example.com\",\"age\":35}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    private UserDTO createTestUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setAge(30);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
