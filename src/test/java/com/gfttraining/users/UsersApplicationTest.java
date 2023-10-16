package com.gfttraining.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersApplicationTest {

    @Test
    @DisplayName("Execution of main method")
    void testMainApplication() {
        assertDoesNotThrow(() -> UsersApplication.main(new String[]{}));
    }
}
