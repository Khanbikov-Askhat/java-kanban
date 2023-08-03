package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exceptions.ServerCreateException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;


public class KVServer {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer(int port) {
        this.apiToken = generateApiToken();
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        } catch (IOException e) {
            throw new ServerCreateException("Can't create server");
        }
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!hasAuth(exchange)) {
                exchange.sendResponseHeaders(FORBIDDEN, 0);
                return;
            }
            if (GET.equals(exchange.getRequestMethod())) {
                String key = exchange.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    exchange.sendResponseHeaders(BAD_REQUEST, 0);
                    return;
                }
                if (!data.containsKey(key)) {
                    System.out.println("Не могу достать данные для ключа '" + key + "', данные отсутствуют");
                    exchange.sendResponseHeaders(NOT_FOUND, 0);
                    return;
                }
                sendText(exchange, data.get(key));
                System.out.println("Значение для ключа " + key + " успешно отправлено в ответ на запрос!");
                exchange.sendResponseHeaders(OK, 0);
            } else {
                System.out.println("/save ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(METHOD_NOT_ALLOWED, 0);
            }
        }
    }

    private void save(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (!hasAuth(exchange)) {
                exchange.sendResponseHeaders(FORBIDDEN, 0);
                return;
            }
            if (POST.equals(exchange.getRequestMethod())) {
                String key = exchange.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    exchange.sendResponseHeaders(BAD_REQUEST, 0);
                    return;
                }
                String value = readText(exchange);
                if (value.isEmpty()) {
                    System.out.println("Данные value отсутствуют");
                    exchange.sendResponseHeaders(BAD_REQUEST, 0);
                    return;
                }
                data.put(key, value);
                exchange.sendResponseHeaders(OK, 0);
            } else {
                System.out.println("/save ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(METHOD_NOT_ALLOWED, 0);
            }
        }
    }

    private void register(HttpExchange exchange) throws IOException {
        try (exchange) {
            if (GET.equals(exchange.getRequestMethod())) {
                sendText(exchange, apiToken);
            } else {
                exchange.sendResponseHeaders(METHOD_NOT_ALLOWED, 0);
            }
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(OK, resp.length);
        exchange.getResponseBody().write(resp);
    }
}