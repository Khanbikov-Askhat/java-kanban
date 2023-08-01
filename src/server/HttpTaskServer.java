package server;


import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import task.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager manager;
    private final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler()); //Путь для задач
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() { // Метод для остановки сервера
        httpServer.stop(1);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }

    public class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI requestURI = httpExchange.getRequestURI(); // Получить URI, по которому был отправлен запрос
            String query = requestURI.getQuery(); // Запрос
            Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
            //String[] pathPart = httpExchange.getRequestURI().getPath().split("/");
            String parameterPart = httpExchange.getRequestURI().getPath();
            //URLEncoder.encode(requestURI.getQuery(), UTF_8.toString())
            String body = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
            //String testQuery = requestURI.getQuery();
            //int test = Integer.parseInt(testQuery.split("=")[1]);
            //boolean a = (String.valueOf(parameterPart.split("=")[1]).isEmpty());
            int id;
            switch (endpoint) {
                case GET_ALL_TASKS: // Для получения данных должны быть GET-запросы
                    writeResponse(httpExchange, gson.toJson(manager.getPrioritizedTasks()), 200);
                    break;
                case GET_HISTORY:
                    writeResponse(httpExchange, gson.toJson(manager.getDefaultHistory()), 200);
                    break;
                case GET_TASKS:
                    //writeResponse(httpExchange, gson.toJson(a), 200);
                    //writeResponse(httpExchange, gson.toJson(requestURI.getQuery()), 200);
                    writeResponse(httpExchange, gson.toJson(manager.getTasks()), 200);
                    break;
                case GET_EPICS:
                    writeResponse(httpExchange, gson.toJson(manager.getEpics()), 200);
                    break;
                case GET_SUBTASKS:
                    writeResponse(httpExchange, gson.toJson(manager.getSubtasks()), 200);
                    break;
                case GET_SUBTASKS_EPIC:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Некорректный id", 400);
                        return;
                    }
                    if (manager.getEpic(id) != null) {
                        writeResponse(httpExchange, gson.toJson(manager.getEpicsSubtasks(id)), 200);
                    } else {
                        writeResponse(httpExchange, "Эпик с id " + id + " не найден", 404);
                    }
                    break;
                case GET_TASK_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id.", 400);
                        return;
                    }
                    Task task = manager.getTask(id);
                    if (task != null) {
                        writeResponse(httpExchange, gson.toJson(task), 200);
                    } else {
                        writeResponse(httpExchange, "Задача с данным id не найдена.", 200);
                    }
                    break;
                case GET_EPIC_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id.", 400);
                        return;
                    }
                    Epic epic = manager.getEpic(id);
                    if (epic != null) {
                        writeResponse(httpExchange, gson.toJson(epic), 200);
                    } else {
                        writeResponse(httpExchange, "Эпик с данным id не найден.", 400);
                    }
                    break;
                case GET_SUBTASK_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id.", 400);
                        return;
                    }
                    Subtask subtask = manager.getSubtask(id);
                    if (subtask != null) {
                        writeResponse(httpExchange, gson.toJson(subtask), 200);
                    } else {
                        writeResponse(httpExchange, "Подзадача с данным id не найдена.", 400);
                    }
                    break;
                case POST_TASK_ID: //  Для создания и изменения — POST-запросы
                    InputStream inputStreamTask = httpExchange.getRequestBody();
                    String bodyTask = new String(inputStreamTask.readAllBytes(), UTF_8);
                    if (bodyTask.isEmpty()) {
                        writeResponse(httpExchange, "Необходимо заполнить все поля задачи", 400);
                        return;
                    }
                    try {
                        task = gson.fromJson(bodyTask, Task.class);
                        if (manager.getTask(task.getId()) == null) {
                            manager.addTask(task);
                            writeResponse(httpExchange, "Задача добавлена", 201);
                        } else {
                            manager.updateTask(task, "NEW");
                            writeResponse(httpExchange, "Задача обновлена", 201);
                        }
                    } catch (JsonSyntaxException e) {
                        writeResponse(httpExchange, "Получен некорректный JSON", 400);
                    }
                    break;
                case POST_EPIC_ID:
                    try {
                        epic = gson.fromJson(body, Epic.class);
                        if (manager.getEpic(epic.getId()) == null) {
                            manager.addEpic(epic);
                            writeResponse(httpExchange, "Эпик добавлен.", 201);
                        } else {
                            manager.updateEpic(epic);
                            writeResponse(httpExchange, "Задача обновлена.", 201);
                        }
                    } catch (JsonSyntaxException | IOException e) {
                        writeResponse(httpExchange, "Неверно проходит POST.", 400);
                    }
                    break;
                case POST_SUBTASK_ID:
                    try {
                        subtask = gson.fromJson(body, Subtask.class);
                        if (manager.getSubtask(subtask.getId()) == null) {
                            manager.addSubtask(subtask);
                            writeResponse(httpExchange, "Подзадача добавлена.", 201);
                        } else {
                            manager.updateSubtask(subtask, "NEW");
                            writeResponse(httpExchange, "Подзадача обновлена.", 201);
                        }
                    } catch (JsonSyntaxException | IOException e) {
                        writeResponse(httpExchange, "Неверно проходит POST.", 400);
                    }
                    break;
                case DELETE_TASKS:
                    manager.deleteTasks();
                    if (manager.getTasks().isEmpty()) {
                        int tasksListSize = manager.getTasks().size();
                        writeResponse(httpExchange, String.valueOf(tasksListSize), 200);
                        //writeResponse(httpExchange, "Все задачи удалены.", 200);
                    }
                    break;
                case DELETE_EPICS:
                    manager.deleteEpics();
                    if (manager.getEpics().isEmpty()) {
                        writeResponse(httpExchange, "Все эпики удалены.", 200);
                    }
                    break;
                case DELETE_SUBTASKS:
                    manager.deleteSubtasks();
                    if (manager.getSubtasks().isEmpty()) {
                        writeResponse(httpExchange, "Все подзадачи удалены.", 200);
                    }
                    break;
                case DELETE_TASK_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id", 400);
                        return;
                    }
                    if (manager.getTask(id) != null) {
                        manager.deleteTask(id);
                        writeResponse(httpExchange, "Задача успешно удалена.", 200);
                    } else {
                        writeResponse(httpExchange, "Неверный формат id", 400);
                    }
                    break;
                case DELETE_EPIC_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id", 400);
                        return;
                    }
                    if (manager.getEpic(id) != null) {
                        manager.deleteEpic(id);
                        writeResponse(httpExchange, "Эпик успешно удален.", 200);
                    } else {
                        writeResponse(httpExchange, "Неверный формат id", 400);
                    }
                    break;
                case DELETE_SUBTASK_ID:
                    id = getId(query);
                    if (id == -1) {
                        writeResponse(httpExchange, "Неверный id", 400);
                        return;
                    }
                    if (manager.getSubtask(id) != null) {
                        manager.deleteSubtask(id);
                        writeResponse(httpExchange, "Задача успешно удалена.", 200);
                    } else {
                        writeResponse(httpExchange, "Неверный формат id", 400);
                    }
                    break;
                default:
                    writeResponse(httpExchange, "Эндпоинт отсутствует.", 404);
                    break;
            }
        }

        private Endpoint getEndpoint(String path, String requestMethod) { //Объединила показатели
            String[] pathPart = path.split("/");

            switch (requestMethod) {
                case "GET":
                    switch (pathPart.length) {
                        case (2):
                            return Endpoint.GET_ALL_TASKS;
                        case (3):
                            if (pathPart[2].equals("history")) {
                                return Endpoint.GET_HISTORY;
                            }
                            if (pathPart[2].equals("task")) {
                                //if (!(String.valueOf(path.split("=")[1]).isEmpty())) {
                                    //return Endpoint.GET_TASK_ID;
                                //} else {
                                return Endpoint.GET_TASKS;
                                //}
                                //return Endpoint.GET_TASKS;
                            }
                            if (pathPart[2].equals("epic")) {
                                return Endpoint.GET_EPICS;
                            }
                            if (pathPart[2].equals("subtask")) {
                                return Endpoint.GET_SUBTASKS;
                            }
                            break;
                        case (4):
                            if (pathPart[2].equals("task")) {
                                return Endpoint.GET_TASK_ID;
                            }
                            if (pathPart[2].equals("epic")) {
                                return Endpoint.GET_EPIC_ID;
                            }
                            if (pathPart[2].equals("subtask")) {
                                return Endpoint.GET_SUBTASK_ID;
                            }
                            break;
                    }
                case "POST":
                    if (pathPart.length == 4 && pathPart[2].equals("task")) {
                        return Endpoint.POST_TASK_ID;
                    }
                    if (pathPart.length == 4 && pathPart[2].equals("epic")) {
                        return Endpoint.POST_EPIC_ID;
                    }
                    if (pathPart.length == 4 && pathPart[2].equals("subtask")) {
                        return Endpoint.POST_SUBTASK_ID;
                    }
                    break;
                case "DELETE":
                    switch (pathPart.length) {
                        case (3):
                            if (pathPart[2].equals("task")) {
                                return Endpoint.DELETE_TASKS;
                            }
                            if (pathPart[2].equals("epic")) {
                                return Endpoint.DELETE_EPICS;
                            }
                            if (pathPart[2].equals("subtask")) {
                                return Endpoint.DELETE_EPICS;
                            }
                            break;
                        case (4):
                            if (pathPart[2].equals("task")) {
                                return Endpoint.DELETE_TASK_ID;
                            }
                            if (pathPart[2].equals("epic")) {
                                return Endpoint.DELETE_EPIC_ID;
                            }
                            if (pathPart[2].equals("subtask")) {
                                return Endpoint.DELETE_SUBTASK_ID;
                            }
                            break;
                    }
                    break;
            }
            return Endpoint.UNKNOWN;
        }

        private void writeResponse(HttpExchange httpExchange, String response, int statusCode) throws IOException {
            byte[] bytes = response.getBytes(UTF_8);
            httpExchange.sendResponseHeaders(statusCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
            httpExchange.close();
        }

        private <T> void foundedTask(T task, HttpExchange httpExchange) throws IOException {
            httpExchange.sendResponseHeaders(200, 0);
            String toJson = gson.toJson(task);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(toJson.getBytes());
            }
        }

        private int getId(String query) {
            try {
                int id = Integer.parseInt(query.split("=")[1]);
                return id;
            } catch (NumberFormatException exception) {
                return -1;
            }
        }
    }
}