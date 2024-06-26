package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import resources.HttpStatusCode;
import server.handler.exeption.IllegalIdExeption;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.SubTask;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class SubtasksHandler extends Handler {
    private static final String SUBTASKS = "/subtasks/";

    private final Type typeSubtask = new TypeToken<SubTask>() {
    }.getType();

    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            SubTask subtask;
            String response;
            final String path = exchange.getRequestURI().getPath();
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst(SUBTASKS, "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                            break;
                        }

                        response = gson.toJson(manager.getSubTaskById(id));
                        if (response != null) {
                            sendResponse(exchange, response);
                        } else {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                        }

                    } else if (Pattern.matches("^/subtasks$", path)) {
                        response = gson.toJson(manager.getAllSubTasks().values().stream().toList());
                        sendResponse(exchange, response);
                    } else {
                        exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);

                    }
                    break;
                }

                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst(SUBTASKS, "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                            break;
                        }

                        manager.removeSubTaskById(id);
                        exchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), 0);

                    } else {
                        exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/subtasks$", path)) {
                        String body = readResponse(exchange);
                        if (body != null) {
                            subtask = gson.fromJson(body, typeSubtask);
                            boolean isCrossing = manager.isCrossing(subtask);
                            if (!isCrossing) {
                                response = gson.toJson(subtask);
                                sendResponse(exchange, response);
                            } else {
                                exchange.sendResponseHeaders(HttpStatusCode.NOT_ACCEPTABLE.getCode(), 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), 0);

                        }
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String body = readResponse(exchange);
                        if (body != null) {
                            subtask = gson.fromJson(body, typeSubtask);
                            manager.updateSubTask(subtask);
                            response = gson.toJson(subtask);
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
            System.out.print(exception.getMessage());
        }
    }

}