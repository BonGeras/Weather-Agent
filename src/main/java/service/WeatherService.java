package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.CurrentWeatherResponse;
import model.ForecastResponse;
import model.WeatherComponents;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public Optional<CurrentWeatherResponse> getCurrentWeather(String city) {
        String url = String.format("%s/weather?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);
        return makeRequest(url, CurrentWeatherResponse.class);
    }

    public Optional<ForecastResponse> getForecast(String city) {
        String url = String.format("%s/forecast?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);
        return makeRequest(url, ForecastResponse.class);
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

    public String formatCurrentWeather(CurrentWeatherResponse weather) {
        if (weather == null || weather.getMain() == null || weather.getWeather() == null || weather.getWeather().isEmpty()) {
            return "Unable to get weather data";
        }

        WeatherComponents.Main main = weather.getMain();
        WeatherComponents.Weather weatherInfo = weather.getWeather().get(0);
        WeatherComponents.Wind wind = weather.getWind();
        WeatherComponents.Clouds clouds = weather.getClouds();

        StringBuilder sb = new StringBuilder();
        sb.append("Current weather in ").append(weather.getName()).append(":\n");
        sb.append(String.format("Temperature: %.1f°C (feels like %.1f°C)\n", main.getTemp(), main.getFeelsLike()));
        sb.append(String.format("Humidity: %d%%\n", main.getHumidity()));
        sb.append(String.format("Wind: %.1f m/s\n", wind.getSpeed()));
        sb.append(String.format("Cloudiness: %d%%\n", clouds.getAll()));
        sb.append(String.format("Conditions: %s\n", weatherInfo.getDescription()));
        
        if (weather.getRain() != null) {
            sb.append(String.format("Rain (1h): %.1f mm\n", weather.getRain().getOneHour()));
        }

        return sb.toString();
    }

    public String formatForecast(ForecastResponse forecast, int hours) {
        if (forecast == null || forecast.getList() == null || forecast.getList().isEmpty()) {
            return "Unable to get forecast data";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Weather forecast for ").append(forecast.getCity().getName()).append(":\n\n");

        forecast.getList().stream()
                .limit(hours / 3)
                .forEach(item -> {
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(item.getDt()),
                            ZoneId.systemDefault()
                    );

                    WeatherComponents.Main main = item.getMain();
                    WeatherComponents.Weather weather = item.getWeather().get(0);
                    WeatherComponents.Wind wind = item.getWind();
                    WeatherComponents.Clouds clouds = item.getClouds();

                    sb.append(String.format("%s:\n", dateTime));
                    sb.append(String.format("Temperature: %.1f°C (feels like %.1f°C)\n", main.getTemp(), main.getFeelsLike()));
                    sb.append(String.format("Humidity: %d%%\n", main.getHumidity()));
                    sb.append(String.format("Wind: %.1f m/s\n", wind.getSpeed()));
                    sb.append(String.format("Cloudiness: %d%%\n", clouds.getAll()));
                    sb.append(String.format("Conditions: %s\n", weather.getDescription()));
                    
                    if (item.getRain() != null) {
                        sb.append(String.format("Rain (3h): %.1f mm\n", item.getRain().getThreeHours()));
                    }
                    sb.append("\n");
                });

        return sb.toString();
    }
} 