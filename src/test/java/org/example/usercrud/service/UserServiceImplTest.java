package org.example.usercrud.service;

import org.example.usercrud.dao.UserDao;
import org.example.usercrud.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User validUser;
    private User invalidUser;

    @BeforeEach
    void setUp() {
        validUser = new User("John Doe", "john@example.com", 25);
        invalidUser = new User("", "invalid-email", -1);
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {

        when(userDao.findById(1L)).thenReturn(Optional.of(validUser));


        Optional<User> result = userService.findById(1L);


        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);

        assertFalse(result.isPresent());
        verify(userDao, times(1)).findById(99L);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {

        User user1 = new User("User One", "user1@example.com", 30);
        User user2 = new User("User Two", "user2@example.com", 25);
        List<User> users = Arrays.asList(user1, user2);


        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));


        verify(userDao, times(1)).findAll();
    }

    @Test
    void create_ShouldSaveValidUser() {
        when(userDao.save(any(User.class))).thenReturn(validUser);

        User result = userService.create(validUser);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertNotNull(result.getCreatedAt());
        verify(userDao, times(1)).save(validUser);
    }

    @Test
    void create_ShouldThrowWhenNameInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.create(new User("", "test@test.com", 20)));

        verify(userDao, never()).save(any());
    }

    @Test
    void create_ShouldThrowWhenEmailInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.create(new User("Valid", "invalid", 20)));

        verify(userDao, never()).save(any());
    }

    @Test
    void create_ShouldThrowWhenAgeInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.create(new User("Valid", "test@test.com", -1)));

        verify(userDao, never()).save(any());
    }

    @Test
    void update_ShouldUpdateValidUser() {
        User updatedUser = new User("Updated", "updated@test.com", 30);
        when(userDao.update(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(updatedUser);

        assertEquals("Updated", result.getName());
        verify(userDao, times(1)).update(updatedUser);
    }

    @Test
    void delete_ShouldCallDaoDelete() {
        doNothing().when(userDao).delete(1L);

        userService.delete(1L);

        verify(userDao, times(1)).delete(1L);
    }
}