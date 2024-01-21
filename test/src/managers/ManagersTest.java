package managers;


import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    //Проверка инициализации
    @Test
    void shouldNotBeNullInGetDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldNotBeNullInGetDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory());
    }
}