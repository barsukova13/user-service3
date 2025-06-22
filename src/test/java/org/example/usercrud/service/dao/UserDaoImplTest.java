package org.example.usercrud.service.dao;

import org.example.usercrud.dao.UserDao;
import org.example.usercrud.dao.UserDaoImpl;
import org.example.usercrud.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

    @Testcontainers
    public class UserDaoImplTest {

        @Container
        private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");

        private static SessionFactory sessionFactory;
        private UserDaoImpl userDao;

        @BeforeAll
        static void setup() {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
            configuration.setProperty("hibernate.connection.username", postgres.getUsername());
            configuration.setProperty("hibernate.connection.password", postgres.getPassword());
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        }

        @BeforeEach
        void init() {
            userDao = new UserDaoImpl(sessionFactory);
        }

        @AfterAll
        static void tearDown() {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }

        @Test
        void shouldSaveAndFindUser() {
            User user = new User("Test User", "test@example.com", 30);
            User savedUser = userDao.save(user);

            assertNotNull(savedUser.getId());
            assertNotNull(savedUser.getCreatedAt());

            Optional<User> foundUser = userDao.findById(savedUser.getId());
            assertTrue(foundUser.isPresent());
            assertEquals("Test User", foundUser.get().getName());
            assertEquals("test@example.com", foundUser.get().getEmail());
        }

        @Test
        void shouldUpdateUser() {
            User user = new User("Original", "original@example.com", 25);
            User savedUser = userDao.save(user);

            savedUser.setName("Updated");
            savedUser.setEmail("updated@example.com");
            savedUser.setAge(30);
            userDao.update(savedUser);

            Optional<User> updatedUser = userDao.findById(savedUser.getId());
            assertTrue(updatedUser.isPresent());
            assertEquals("Updated", updatedUser.get().getName());
            assertEquals("updated@example.com", updatedUser.get().getEmail());
            assertEquals(30, updatedUser.get().getAge());
        }

        @Test
        void shouldDeleteUser() {
            User user = new User("To Delete", "delete@example.com", 40);
            User savedUser = userDao.save(user);

            userDao.delete(savedUser.getId());

            Optional<User> deletedUser = userDao.findById(savedUser.getId());
            assertFalse(deletedUser.isPresent());
        }

        @Test
        void shouldFindAllUsers() {
            userDao.save(new User("User1", "user1@example.com", 20));
            userDao.save(new User("User2", "user2@example.com", 25));

            List<User> users = userDao.findAll();

            assertEquals(2, users.size());
            assertTrue(users.stream().anyMatch(u -> u.getName().equals("User1")));
            assertTrue(users.stream().anyMatch(u -> u.getName().equals("User2")));
        }

        @Test
        void shouldReturnEmptyWhenUserNotFound() {
            Optional<User> user = userDao.findById(999L);
            assertFalse(user.isPresent());
        }}
