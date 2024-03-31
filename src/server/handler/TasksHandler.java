package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import resources.HttpStatusCode;
import server.handler.exeption.IllegalIdExeption;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Task;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class TasksHandler extends Handler {
    private static final String TASKS = "/tasks/";
    private final Type typeTask = new TypeToken<Task>() {
    }.getType();

    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            Task task;
            String response;
            final String path = exchange.getRequestURI().getPath();
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst(TASKS, "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                            break;
                        }
                        response = gson.toJson(manager.getTaskById(id));
                        if (response != null) {
                            sendResponse(exchange, response);
                        } else {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                        }
                    } else if (Pattern.matches("^/tasks$", path)) {
                        response = gson.toJson(manager.getAllTasks().values().stream().toList());
                        sendResponse(exchange, response);
                    } else {
                        exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);
                    }
                    break;
                }

                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst(TASKS, "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                            break;
                        }
                        manager.removeTaskById(id);
                        exchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), 0);

                    } else {
                        exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getCode(), 0);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String body = readResponse(exchange);
                        if (body != null) {
                            task = gson.fromJson(body, typeTask);


                            boolean isCrossing = manager.isCrossing(task);
                            if (!isCrossing) {
                                manager.createNewTask(task);
                                response = gson.toJson(task);
                                sendResponse(exchange, response);
                            } else {
                                exchange.sendResponseHeaders(HttpStatusCode.NOT_ACCEPTABLE.getCode(), 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);
                        }
                    } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String body = readResponse(exchange);
                        if (body != null) {
                            task = gson.fromJson(body, typeTask);
                            manager.updateTask(task);
                            response = gson.toJson(task);
                            sendResponse(exchange, response);
                        } else {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                        }

                    } else {
                        exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);
                    }
                }
                break;
                default: {
                    exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getCode(), 0);
                    break;
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}