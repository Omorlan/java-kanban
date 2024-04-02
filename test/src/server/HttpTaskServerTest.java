package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sprint.managers.Managers;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskServerTest {

    private HttpTaskServer taskServer;
    private TaskManager manager;
    private Epic epic;
    private Task task;
    private HttpClient client;
    private SubTask subtask;
    private Gson gson;
    private static final int PORT = 8080;
    private static final String HOST = "http://localhost:";
    private static final String HOST_PORT = HOST + PORT;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        taskServer = new HttpTaskServer(manager);
        gson = Managers.getGson();
        client = HttpClient.newHttpClient();
        epic = new Epic("Epic name", "Epic Description");
        task = new Task("Task name", "Task Description", TaskStatus.NEW, 15, "17.03.2024 03:00");
        subtask = new SubTask("SubTask Name", "SubTask Description", TaskStatus.NEW, epic, 15, "17.03.2024 03:00");
        taskServer.start();
    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }

    private String toJsonString(Object object) {
        return gson.toJson(object);
    }

    private HttpResponse<String> sendGetRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDeleteRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendPostRequest(URI uri, String requestBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void assertStatusCode(int expectedStatus, HttpResponse<?> response) {
        assertEquals(expectedStatus, response.statusCode());
    }

    @Nested
    @DisplayName("GET method tests")
    class GetTests {

        @Test
        @DisplayName("GET should retrieve SubTask by ID")
        void shouldRetrieveSubTaskById() throws IOException, InterruptedException {
            Type type = new TypeToken<SubTask>() {
            }.getType();
            manager.createNewSubTask(subtask);
            URI url = URI.create(HOST_PORT + "/subtasks/0");
            HttpResponse<String> responseGetId = sendGetRequest(url);
            assertEquals(200, responseGetId.statusCode());
            final SubTask actualSubtask = gson.fromJson(responseGetId.body(), type);
            assertEquals(manager.getSubTaskById(subtask.getId()), actualSubtask, "Subtasks are different");
        }

        @Test
        @DisplayName("GET should retrieve Task by ID")
        void shouldRetrieveTaskById() throws IOException, InterruptedException {
            Type type = new TypeToken<Task>() {
            }.getType();
            manager.createNewTask(task);
            URI url = URI.create(HOST_PORT + "/tasks/0");
            HttpResponse<String> responseGetId = sendGetRequest(url);
            assertEquals(200, responseGetId.statusCode());
            final Task actualTask = gson.fromJson(responseGetId.body(), type);
            assertEquals(manager.getTaskById(task.getId()), actualTask, "Tasks are different");
        }

        @Test
        @DisplayName("Get should retrieve Epics, History, and Prioritized")
        void shouldRetrieveEpicsHistoryAndPrioritized() throws IOException, InterruptedException {
            Type epicType = new TypeToken<Epic>() {
            }.getType();
            Type epicsType = new TypeToken<List<Epic>>() {
            }.getType();

            manager.createNewEpic(epic);

            URI url = URI.create(HOST_PORT + "/epics");
            HttpResponse<String> responseGet = sendGetRequest(url);
            assertStatusCode(200, responseGet);
            final List<Epic> actualEpics = gson.fromJson(responseGet.body(), epicsType);
            assertEquals(manager.getAllEpics().values().stream().toList(), actualEpics, "Lists of epics do not match");

            url = URI.create(HOST_PORT + "/epics/0");
            HttpResponse<String> responseGetId = sendGetRequest(url);
            assertStatusCode(200, responseGetId);
            final Epic actualEpic = gson.fromJson(responseGetId.body(), epicType);
            final List<Epic> epicsFromManager = manager.getAllEpics().values().stream().toList();
            assertEquals(1, epicsFromManager.size(), "Incorrect number of epics");
            assertEquals(actualEpic.getName(), epic.getName(), "Incorrect epic name");
            manager.createNewSubTask(subtask);

            url = URI.create(HOST_PORT + "/epics/0/subtasks");
            HttpResponse<String> responseGetEpicSb = sendGetRequest(url);
            assertStatusCode(200, responseGetEpicSb);
            final List<Integer> actualEpicSb = gson.fromJson(responseGetEpicSb.body(), new TypeToken<List<Integer>>() {
            }.getType());
            assertEquals(epic.getSubTaskIds(), actualEpicSb, "Subtask list does not match");

            url = URI.create(HOST_PORT + "/prioritized");
            HttpResponse<String> responseGetPriority = sendGetRequest(url);
            assertStatusCode(200, responseGetPriority);
            final Set<Task> actualPriority = gson.fromJson(responseGetPriority.body(), new TypeToken<Set<Task>>() {
            }.getType());
            assertEquals(manager.getPrioritizedTasks(), actualPriority, "Prioritized lists do not match");
        }
    }

    @Nested
    @DisplayName("POST method tests")
    class PostTests {

        @Test
        @DisplayName("POST should create new Epic")
        void shouldCreateNewEpic() throws IOException, InterruptedException {
            URI url = URI.create("http://localhost:" + PORT + "/epics");
            HttpResponse<String> responsePost = sendPostRequest(url, toJsonString(epic));
            assertStatusCode(200, responsePost);
            final Epic actualEpic = gson.fromJson(responsePost.body(), new TypeToken<Epic>() {
            }.getType());
            assertNotNull(manager.getAllEpics(), "Epics are not returned");
            assertEquals(manager.getEpicById(0), actualEpic, "Epics do not match");
        }

        @Test
        @DisplayName("POST should create new Task")
        void shouldCreateNewTask() throws IOException, InterruptedException {
            URI url = URI.create(HOST_PORT + "/tasks");
            HttpResponse<String> responsePost = sendPostRequest(url, toJsonString(task));
            assertStatusCode(200, responsePost);
            assertNotNull(manager.getAllTasks(), "Tasks are not returned");
        }

        @Test
        @DisplayName("POST should create new SubTask")
        void shouldCreateNewSubTask() throws IOException, InterruptedException {
            manager.createNewEpic(epic);
            URI url = URI.create(HOST_PORT + "/subtasks");
            HttpResponse<String> responsePost = sendPostRequest(url, toJsonString(subtask));
            assertEquals(200, responsePost.statusCode());
            assertNotNull(manager.getAllSubTasks(), "Subtasks are not returned");
            SubTask subtask2 = new SubTask(1, "Name", "Description", TaskStatus.DONE, epic, 15, "17.03.2024 03:00");
            String newSubtaskJson = gson.toJson(subtask2);
            url = URI.create(HOST_PORT + "/subtasks/1");
            HttpResponse<String> responsePostId = sendPostRequest(url, newSubtaskJson);
            assertStatusCode(200, responsePostId);
            assertEquals(manager.getSubTaskById(1), subtask2, "Subtasks are different");
        }

        @Test
        @DisplayName("Post should update Task by ID")
        void shouldUpdateTaskById() throws IOException, InterruptedException {
            manager.createNewTask(task);
            Task task2 = new Task(task.getId(), "Task Name", "Description task", TaskStatus.DONE, 15, "17.03.2024 03:00");
            URI url = URI.create(HOST_PORT + "/tasks/0");
            HttpResponse<String> responsePost = sendPostRequest(url, toJsonString(task2));
            assertStatusCode(200, responsePost);
            assertEquals(manager.getTaskById(0), task2, "Tasks are different");
        }
    }

    @Nested
    @DisplayName("DELETE method tests")
    class DeleteTests {

        @Test
        @DisplayName("Delete should delete Task by ID")
        void shouldDeleteTaskById() throws IOException, InterruptedException {
            manager.createNewTask(task);
            URI url = URI.create(HOST_PORT + "/tasks/0");
            HttpResponse<String> responseDelete = sendDeleteRequest(url);
            assertEquals(200, responseDelete.statusCode());
            assertTrue(manager.getAllTasks().isEmpty(), "Tasks are not empty");
        }

        @Test
        @DisplayName("DELETE should delete Epic by ID")
        void shouldDeleteEpicById() throws IOException, InterruptedException {
            manager.createNewEpic(epic);
            URI url = URI.create(HOST_PORT + "/epics/0");
            HttpResponse<String> responseDelete = sendDeleteRequest(url);
            assertEquals(200, responseDelete.statusCode());
            assertTrue(manager.getAllEpics().isEmpty(), "Epics are not empty");
        }

        @Test
        @DisplayName("DELETE should delete SubTask by ID")
        void shouldDeleteSubTaskById() throws IOException, InterruptedException {
            manager.createNewEpic(epic);
            manager.createNewSubTask(subtask);
            URI url = URI.create(HOST_PORT + "/subtasks/1");
            HttpResponse<String> responseDelete = sendDeleteRequest(url);
            assertEquals(200, responseDelete.statusCode());
            assertTrue(manager.getAllSubTasks().isEmpty(), "Subtasks are not empty");
        }
    }

    @Test
    @DisplayName("Crossing tasks should throw 406 code")
    void crossingTaskShouldThrow406Code() throws IOException, InterruptedException {
        manager.createNewEpic(epic);
        manager.createNewSubTask(subtask);
        SubTask checkSubtask = new SubTask("Name", "Description", TaskStatus.NEW, epic, 15, "17.03.2024 03:00");
        String subtaskJson = gson.toJson(checkSubtask);
        URI url = URI.create(HOST_PORT + "/subtasks");
        HttpResponse<String> responsePost = sendPostRequest(url, subtaskJson);
        assertEquals(406, responsePost.statusCode());
    }

    @Test
    @DisplayName("Should return correct history after update")
    void shouldReturnCorrectHistoryAfterUpdate() throws IOException, InterruptedException {
        manager.createNewEpic(epic);
        manager.getEpicById(epic.getId());
        URI url = URI.create(HOST_PORT + "/history");
        HttpResponse<String> responseGetHistoryBefore = sendGetRequest(url);
        assertEquals(200, responseGetHistoryBefore.statusCode());
        final List<Task> actualHistoryBefore = gson.fromJson(responseGetHistoryBefore.body(),
                new TypeToken<List<Task>>() {
                }.getType());
        assertEquals(manager.getHistory(), actualHistoryBefore, "Histories do not match");
        manager.createNewTask(task);
        manager.getTaskById(task.getId());
        HttpResponse<String> responseGetHistoryAfter = sendGetRequest(url);
        assertEquals(200, responseGetHistoryAfter.statusCode());
        final List<Task> actualHistoryAfter = gson.fromJson(responseGetHistoryAfter.body(),
                new TypeToken<List<Task>>() {
                }.getType());
        assertEquals(manager.getHistory(), actualHistoryAfter, "Histories do not match");
    }
}
