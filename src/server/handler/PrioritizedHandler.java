package server.handler;

import com.sun.net.httpserver.HttpExchange;
import resources.HttpStatusCode;
import sprint.managers.taskmanager.TaskManager;

import java.util.regex.Pattern;

public class PrioritizedHandler extends Handler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            String response;
            final String path = exchange.getRequestURI().getPath();
            if (!exchange.getRequestMethod().equals("GET")) {
                exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getCode(), 0);
                return;
            }
            if (Pattern.matches("^/prioritized$", path)) {
                response = gson.toJson(manager.getPrioritizedTasks());
                sendResponse(exchange, response);
            } else {
                exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), 0);
            }
        } catch (Exception exception) {
            System.out.print(exception.getMessage());
        }
    }
}