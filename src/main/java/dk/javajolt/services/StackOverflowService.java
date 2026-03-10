package dk.javajolt.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.javajolt.dtos.StackOverflowQuestionDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StackOverflowService {

    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<StackOverflowQuestionDTO> fetchQuestions(String tag, int pageSize) throws Exception {
        String url = BASE_URL + "/questions?order=desc&sort=votes&tagged="
                + tag + "&site=stackoverflow&pagesize=" + pageSize + "&filter=withbody";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept-Encoding", "identity")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());
        JsonNode items = root.get("items");

        List<StackOverflowQuestionDTO> questions = new ArrayList<>();
        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                questions.add(mapper.treeToValue(item, StackOverflowQuestionDTO.class));
            }
        }
        return questions;
    }
}