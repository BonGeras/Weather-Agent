package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import model.WeatherResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

public class WeatherService {
    private static final String API_KEY = "587ce6baaad00276cb27b472f828139b";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public WeatherService() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<WeatherResponse> getCurrentWeather(String city) {
        String url = String.format("%s/weather?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);
        try {
            Optional<JsonNode> responseOpt = makeRequest(url, JsonNode.class);
            if (responseOpt.isEmpty()) {
                return Optional.empty();
            }

            JsonNode response = responseOpt.get();
            return parseCurrentWeather(response, city);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<WeatherResponse> getForecast(String city, int forecastIndex) {
        String url = String.format("%s/forecast?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);
        try {
            Optional<JsonNode> responseOpt = makeRequest(url, JsonNode.class);
            if (responseOpt.isEmpty()) {
                return Optional.empty();
            }

            JsonNode response = responseOpt.get();
            JsonNode list = response.get("list");
            if (list == null || !list.isArray() || forecastIndex >= list.size()) {
                return Optional.empty();
            }

            return parseForecastWeather(list.get(forecastIndex), city);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<WeatherResponse> parseCurrentWeather(JsonNode data, String city) {
        try {
            JsonNode main = data.get("main");
            JsonNode weather = data.get("weather").get(0);
            JsonNode wind = data.get("wind");
            JsonNode clouds = data.get("clouds");
            
            WeatherResponse.Builder builder = new WeatherResponse.Builder()
                .temperature(main.get("temp").asDouble())
                .feelsLike(main.get("feels_like").asDouble())
                .humidity(main.get("humidity").asInt())
                .windSpeed(wind.get("speed").asDouble())
                .cloudiness(clouds.get("all").asInt())
                .description(weather.get("description").asText())
                .cityName(city)
                .isCurrentWeather(true);

            if (data.has("rain") && data.get("rain").has("1h")) {
                builder.rain(data.get("rain").get("1h").asDouble());
            }
            
            return Optional.of(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<WeatherResponse> parseForecastWeather(JsonNode data, String city) {
        try {
            JsonNode main = data.get("main");
            JsonNode weather = data.get("weather").get(0);
            JsonNode wind = data.get("wind");
            JsonNode clouds = data.get("clouds");
            
            WeatherResponse.Builder builder = new WeatherResponse.Builder()
                .temperature(main.get("temp").asDouble())
                .feelsLike(main.get("feels_like").asDouble())
                .humidity(main.get("humidity").asInt())
                .windSpeed(wind.get("speed").asDouble())
                .cloudiness(clouds.get("all").asInt())
                .description(weather.get("description").asText())
                .cityName(city)
                .isCurrentWeather(false);

            if (data.has("rain") && data.get("rain").has("3h")) {
                builder.rain(data.get("rain").get("3h").asDouble());
            }

            return Optional.of(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private <T> Optional<T> makeRequest(String url, Class<T> responseType) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return Optional.empty();
            }

            String responseBody = response.body().string();
            return Optional.of(objectMapper.readValue(responseBody, responseType));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
} 