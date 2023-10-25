package com.gfttraining.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersApplicationTest {
    @Test
    @DisplayName("WHEN the application is executed THEN it does not throw any exception")
    void testNoExceptionInExecution() {

        assertDoesNotThrow(() -> UsersApplication.main(new String[]{}));
    }
}