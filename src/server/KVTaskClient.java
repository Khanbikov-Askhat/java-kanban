package server;

import exceptions.NotCorrectRequestException;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVTaskClient {
    private final String url = "http://localhost:8078";
    private final HttpClient httpClient = HttpClient.newHttpClient(); // Финализировала, инкапсулировала
    private String apiToken; // Токен, который нужен при работе с сервером

    public KVTaskClient() { // Cоздаем экземпляр KVTaskClient
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                apiToken = response.body();
            }
            System.out.println("Не удалось получить данные API_TOKEN.");
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" + "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) { // Метод должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "/save" + key + "?API_TOKEN=" + apiToken)).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
            if (response.statusCode() != 200) {
                throw new NotCorrectRequestException("Произошла ошибка при загрузке с сервера. Код ошибки: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            throw new NotCorrectRequestException("Во время выполнения запроса возникла ошибка.");
        }
    }

    public String load(String key) { // Метод должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "/load" + key + "?API_TOKEN=" + apiToken)).header("Content-Type", "application/json").GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new NotCorrectRequestException("Во время выполнения запроса возникла ошибка.");
            }
            return response.body();
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            throw new NotCorrectRequestException("Во время выполнения запроса возникла ошибка.");
        }
    }
}