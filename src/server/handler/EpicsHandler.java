package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import server.handler.exeption.IllegalIdExeption;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class EpicsHandler extends Handler {
    private static final String EPICS = "/epics/";
    private final Type typeEpic = new TypeToken<Epic>() {
    }.getType();

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGetRequest(exchange, path);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange, path);
                    break;
                case "POST":
                    handlePostRequest(exchange, path);
                    break;
                default:
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void handleGetRequest(HttpExchange exchange, String path) throws IOException, IllegalIdExeption {
        if (Pattern.matches("^/epics/\\d+$", path)) {
            String pathId = path.replaceFirst(EPICS, "");
            int id = parsePathId(pathId);
            String response = gson.toJson(manager.getEpicById(id));
            if (response != null) {
                sendResponse(exchange, response);
            } else {
                exchange.sendResponseHeaders(404, 0);
            }
        } else if (Pattern.matches("^/epics$", path)) {
            String response = gson.toJson(manager.getAllEpics().values().stream().toList());
            sendResponse(exchange, response);
        } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
            String pathId = path.replaceFirst(EPICS, "").replaceFirst("/subtasks", "");
            int id = parsePathId(pathId);
            String response = gson.toJson(manager.getEpicById(id).getSubTaskIds());
            if (response != null) {
                sendResponse(exchange, response);
            } else {
                exchange.sendResponseHeaders(404, 0);
            }
        } else {
            exchange.sendResponseHeaders(500, 0);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String path) throws IOException, IllegalIdExeption {
        if (Pattern.matches("^/epics/\\d+$", path)) {
            String pathId = path.replaceFirst(EPICS, "");
            int id = parsePathId(pathId);
            manager.removeEpicById(id);
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        } else {
            exchange.sendResponseHeaders(405, 0);
        }
    }

    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        if (Pattern.matches("^/epics$", path)) {
            String body = readResponse(exchange);
            if (body != null) {
                Epic epic = gson.fromJson(body, typeEpic);
                manager.createNewEpic(epic);
                String response = gson.toJson(epic);
                sendResponse(exchange, response);
            } else {
                exchange.sendResponseHeaders(500, 0);
            }
        } else {
            exchange.sendResponseHeaders(500, 0);
        }
    }
}
