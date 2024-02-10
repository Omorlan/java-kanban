package managers;


import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    //Проверка инициализации
    @Test
    void getDefaultShouldNotBeNull() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void getDefaultHistoryShouldNotBeNullIn() {
        assertNotNull(Managers.getDefaultHistory());
    }
}