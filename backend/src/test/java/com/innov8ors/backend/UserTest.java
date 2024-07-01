package com.innov8ors.backend;
import org.junit.jupiter.api.Test;

import com.innov8ors.backend.model.User;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testCreateUser() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        String password = "secret";
        String role = "user";

        User user = new User(name, email, password, role);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
    }


    @Test
    public void testSetValidEmail() {
        String name = "John Doe";
        String validEmail = "john.doe@example.com";
        String password = "secret";
        String role = "user";

        User user = new User(name, validEmail, password, role);

        assertEquals(validEmail, user.getEmail());
    }
}