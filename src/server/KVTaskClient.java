package server;

import exceptions.*;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiToken;
    private final String url;
    private final int port;

    public KVTaskClient(String url, int port) {
        this.port = port;
        this.url = url;
        URI register = URI.create(url + port + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(register)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TaskClientRegisterException("Can't register " + e.getMessage());
        }
        apiToken = response.body();
    }

    public void put(String key, String json) {
        URI save = URI.create(url + port + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(save)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ManagerServerSaveException("Can't save to server" + e.getMessage());
        }
        if (StringUtils.isBlank(String.valueOf(response)) || response.statusCode() != 200) {
            throw new ManagerServerSaveException("Can't save to server");
        }
    }

    public String load(String key) {
        URI load = URI.create(url + port + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(load)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ManagerLoadException("Can't load from server" + e.getMessage());
        }
        if (StringUtils.isBlank(response.body())) {
            throw new ManagerLoadException("Can't load from server");
        }
        return response.body();
    }
}