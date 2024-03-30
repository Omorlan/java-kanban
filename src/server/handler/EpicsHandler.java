package server.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import server.handler.exeption.IllegalIdExeption;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;

import java.lang.reflect.Type;
import java.util.regex.Pattern;


public class EpicsHandler extends Handler {
    private final Type typeEpic = new TypeToken<Epic>() {
    }.getType();

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            Epic epic;
            String response;
            final String path = exchange.getRequestURI().getPath();
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(manager.getEpicById(id));
                            if (response != null) {
                                sendResponse(exchange, response);
                            } else {
                                System.out.println("Epic with id = " + pathId + "not existing");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            System.out.println("Invalid id received = " + pathId);
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/epics$", path)) {
                        response = gson.toJson(manager.getAllEpics().values().stream().toList());
                        sendResponse(exchange, response);
                    } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.replaceFirst("/epics/", "")
                                .replaceFirst("/subtasks", "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(404, 0);
                            break;
                        }

                        response = gson.toJson(manager.getEpicById(id).getSubTaskIds());
                        if (response != null) {
                            sendResponse(exchange, response);
                        } else {
                            System.out.println("Epic with id = " + pathId + "not existing");
                            exchange.sendResponseHeaders(404, 0);
                        }

                    } else {
                        exchange.sendResponseHeaders(500, 0);
                    }
                    break;
                }

                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id;
                        try {
                            id = parsePathId(pathId);
                        } catch (NumberFormatException | IllegalIdExeption e) {
                            exchange.sendResponseHeaders(404, 0);
                            break;
                        }

                        manager.removeEpicById(id);
                        System.out.println("Delete epic id = " + id);
                        exchange.sendResponseHeaders(200, 0);
                        exchange.close();

                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        String body = readResponse(exchange);
                        if (body != null) {
                            epic = gson.fromJson(body, typeEpic);
                            manager.createNewEpic(epic);
                            response = gson.toJson(epic);
                            sendResponse(exchange, response);
                        } else {
                            exchange.sendResponseHeaders(500, 0);
                        }
                    } else {
                        exchange.sendResponseHeaders(500, 0);
                    }
                    break;
                }
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