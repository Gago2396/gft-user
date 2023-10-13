package com.gfttraining.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersApplicationTest {

    @Test
    void testMainApplication() {
        assertDoesNotThrow(() -> UsersApplication.main(new String[]{}));
    }
}
