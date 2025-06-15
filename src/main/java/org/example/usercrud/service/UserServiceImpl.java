
package org.example.usercrud.service;

import org.example.usercrud.dao.UserDao;
import org.example.usercrud.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        logger.info("Retrieving all users");
        return userDao.findAll();
    }

    @Override
    public User create(User user) {
        validateUser(user);
        logger.info("Creating user: {}", user);
        return userDao.save(user);
    }

    @Override
    public User update(User user) {
        validateUser(user);
        logger.info("Updating user: {}", user);
        return userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting user with id: {}", id);
        userDao.delete(id);
    }

    private void validateUser(User user) {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (user.getAge() == null || user.getAge() <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
    }
}
