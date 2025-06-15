package org.example.usercrud.service;

import org.example.usercrud.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(Long id);
}
