package managers.taskmanager;

import managers.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import sprint.managers.taskmanager.InMemoryTaskManager;

import java.io.IOException;

class InMemoryTaskManagersTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
    }


}
