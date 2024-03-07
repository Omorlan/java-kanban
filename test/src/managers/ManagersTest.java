package managers;


import org.junit.jupiter.api.Test;
import sprint.managers.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void getDefaultShouldNotBeNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void getDefaultHistoryShouldNotBeNullIn() {
        assertNotNull(Managers.getDefaultHistory());
    }
}