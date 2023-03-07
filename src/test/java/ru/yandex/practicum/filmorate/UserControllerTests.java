package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class UserControllerTests {

    UserController controller;
    User user1;
    User user2;
    Validator validator;

    @BeforeEach
    public void beforeEach() {
        user1 = null;
        user2 = null;
        controller = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // User Validation Tests

    @Test
    public void nullEmailValidationTest() {
        user1 = User.builder().name("Test Name").birthday(LocalDate.now().minusYears(10)).login("testLogin").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyEmailValidationTest() {
        user1 = User.builder().email(" ").name("Test Name").birthday(LocalDate.now().minusYears(10)).login("testLogin").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void invalidEmailValidationTest() {
        user1 = User.builder().email("fakemailgmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).login("testLogin").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullLoginValidationTest() {
        user1 = User.builder().email("fakemail@gmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginValidationTest() {
        user1 = User.builder().login(" ").email("fakemail@gmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void birthdayDateInPresentValidationTest() {
        user1 = User.builder().name("Test Name").login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now()).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void birthdayDateInFutureValidationTest() {
        user1 = User.builder().name("Test Name").login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().plusDays(1)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertFalse(violations.isEmpty());
    }

    // Method addUser() Tests

    @Test
    public void addUserTest() {
        user1 = User.builder().name("Test Name").email("testuser@gmail.com").login("testLogin").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());
        assertEquals(user1, controller.getUsers().get(user1.getId()));
    }

    @Test
    public void invalidLoginAddUserTest() {
        user1 = User.builder().login("test login").email("fakemail@gmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).build();
        ValidationException e = assertThrows(ValidationException.class, ()-> controller.addUser(user1));
        assertEquals("Логин не может содержать пробелы.", e.getMessage());
    }

    @Test
    public void nameMustBeEqualsLoginIfNameIsNullAddUserTest() {
        user1 = User.builder().login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());
        assertEquals("testLogin", controller.getUsers().get(user1.getId()).getName());
    }

    @Test
    public void nameMustBeEqualsLoginIfNameIsEmptyAddUserTest() {
        user1 = User.builder().name(" ").login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());
        assertEquals("testLogin", controller.getUsers().get(user1.getId()).getName());
    }

    // Method updateUser() Tests

    @Test
    public void updateUserTest() {
        user1 = User.builder().name("Test Name").email("testuser@gmail.com").login("testLogin").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());
        assertEquals(user1, controller.getUsers().get(user1.getId()));

        user2 = User.builder().id(user1.getId()).name("Test Name 2").email("testuser@gmail.com").login("testLogin2").birthday(LocalDate.now().minusYears(10)).build();
        controller.updateUser(user2);
        assertEquals(1, controller.getUsers().size());
        assertEquals(user2, controller.getUsers().get(user1.getId()));
    }

    @Test
    public void invalidLoginUpdateUserTest() {
        user1 = User.builder().login("testLogin").email("fakemail@gmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());
        user2 = User.builder().id(user1.getId()).login("test login").email("fakemail@gmail.com").name("Test Name").birthday(LocalDate.now().minusYears(10)).build();
        ValidationException e = assertThrows(ValidationException.class, ()-> controller.updateUser(user2));
        assertEquals("Логин не может содержать пробелы.", e.getMessage());
    }

    @Test
    public void nameMustBeEqualsLoginIfNameIsNullUpdateUserTest() {
        user1 = User.builder().login("testLogin").name("Test Name").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());

        user2 = User.builder().id(user1.getId()).login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.updateUser(user2);
        assertEquals(1, controller.getUsers().size());
        assertEquals("testLogin", controller.getUsers().get(user1.getId()).getName());
    }

    @Test
    public void nameMustBeEqualsLoginIfNameIsEmptyUpdateUserTest() {
        user1 = User.builder().name("Test Name").login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        assertEquals(1, controller.getUsers().size());

        user2 = User.builder().id(user1.getId()).name(" ").login("testLogin").email("fakemail@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.updateUser(user2);
        assertEquals(1, controller.getUsers().size());
        assertEquals("testLogin", controller.getUsers().get(user1.getId()).getName());
    }

    // Method getAllUsers() Tests

    @Test
    public void getAllUsersTest() {
        user1 = User.builder().name("Test Name 1").login("testLogin1").email("fakemail1@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        user2 = User.builder().name("Test Name 2").login("testLogin2").email("fakemail2@gmail.com").birthday(LocalDate.now().minusYears(10)).build();
        controller.addUser(user1);
        controller.addUser(user2);
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(user1);
        usersList.add(user2);
        assertEquals(usersList, controller.getAllUsers());
    }







}
