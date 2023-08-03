package server;

import exceptions.*;

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
        URI register = createURI(url, port, "/register");
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
            throw new TaskClientRegisterException("Can't register " + e.getMessage());
        }
        apiToken = response.body();
    }

    public String put(String key, String json) {
        URI save = createOperationURI(url, port, key, "/save/",apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(save)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerServerSaveException("Can't do save request, status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do save request", (IOException) e);
        }
    }

    public String load(String key) {
        URI load = createOperationURI(url, port, key, "/load/",apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(load)
                .header("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerLoadException("Can't load from server");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerLoadException("Can't load from server" + e.getMessage());
        }
    }

    private URI createURI(String url, int port, String key) {
        return  URI.create(url + port + key);
    }

    private URI createOperationURI(String url, int port, String key, String operationToken, String apiToken) {
        return URI.create(url + port + operationToken + key + "?API_TOKEN=" + apiToken);
    }
}
/*
Спасибо за ревью!
В целом я вроде понял как это работает, вроде интересно обрабатывать запросы, но за всем уследить конечно тяжко.
*/