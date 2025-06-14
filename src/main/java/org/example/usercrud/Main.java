package org.example.usercrud;

import org.example.usercrud.dao.UserDaoImpl;
import org.example.usercrud.model.User;
import org.example.usercrud.service.UserServiceImpl;
import org.example.usercrud.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl(sessionFactory));

    public static void main(String[] args) {

        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createUser();
                        break;
                    case "2":
                        findUserById();
                        break;
                    case "3":
                        findAllUsers();
                        break;
                    case "4":
                        updateUser();
                        break;
                    case "5":
                        deleteUser();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            System.out.println("Application closed.");
        }
    }

    private static void printMenu() {
        System.out.println("\n=== User CRUD Menu ===");
        System.out.println("1. Create User");
        System.out.println("2. Find User by ID");
        System.out.println("3. List All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        try {
            User user = new User(name, email, age);
            User savedUser = userService.create(user);
            System.out.println("User created: " + savedUser);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void findUserById() {
        System.out.print("Enter user ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            System.out.println("Found: " + user.get());
        } else {
            System.out.println("User not found!");
        }
    }

    private static void findAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Enter user ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<User> userOpt = userService.findById(id);
        if (!userOpt.isPresent()) {
            System.out.println("User not found!");
            return;
        }

        User user = userOpt.get();
        System.out.println("Current data: " + user);

        System.out.print("New name (" + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) user.setName(name);

        System.out.print("New email (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("New age (" + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) user.setAge(Integer.parseInt(ageInput));

        try {
            User updatedUser = userService.update(user);
            System.out.println("Updated: " + updatedUser);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        try {
            userService.delete(id);
            System.out.println("User deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

