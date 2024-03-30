package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import server.handler.exeption.IllegalIdExeption;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.SubTask;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class SubtasksHandler extends Handler {
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
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(404, 0);
                            break;
                        }

                        response = gson.toJson(manager.getSubTaskById(id));
                        if (response != null) {
                            sendResponse(exchange, response);
                        } else {
                            System.out.println("Subtask with id = " + pathId + "not existing");
                            exchange.sendResponseHeaders(404, 0);
                        }

                    } else if (Pattern.matches("^/subtasks$", path)) {
                        response = gson.toJson(manager.getAllSubTasks().values().stream().toList());
                        sendResponse(exchange, response);
                    } else {
                        exchange.sendResponseHeaders(500, 0);
                    }
                    break;
                }

                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(404, 0);
                            break;
                        }

                        manager.removeSubTaskById(id);
                        System.out.println("Delete subtask id = " + id);
                        exchange.sendResponseHeaders(200, 0);
                        exchange.close();

                    } else {
                        exchange.sendResponseHeaders(405, 0);
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
                                exchange.sendResponseHeaders(406, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(500, 0);
                        }
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(404, 0);
                            break;
                        }
                        String body = readResponse(exchange);

                        if (body != null) {
                            subtask = gson.fromJson(body, typeSubtask);
                            manager.updateSubTask(subtask);
                            response = gson.toJson(subtask);
                            sendResponse(exchange, response);
                        } else {
                            exchange.sendResponseHeaders(500, 0);
                        }

                    } else {
                        exchange.sendResponseHeaders(500, 0);
                    }
                }
                break;
                default: {
                    exchange.sendResponseHeaders(405, 0);
                    break;
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}