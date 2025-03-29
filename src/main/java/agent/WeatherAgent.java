package agent;

import model.WeatherComponents;
import model.ForecastResponse;
import service.WeatherService;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAgent {
    private final WeatherService weatherService;
    private final Scanner scanner;
    private static final Pattern QUERY_PATTERN = Pattern.compile(
        "(?i)determine the (temperature|weather|humidity|wind|clouds|rain) in ([^\\d]+?) in (\\d+ (?:minute|minutes|hour|hours))\\s*$"
    );

    public WeatherAgent() {
        this.weatherService = new WeatherService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Weather Agent started. I can help you determine various weather conditions in any city.");
        System.out.println("Please use the following format:");
        System.out.println("Determine the {condition} in {city} in {time}");
        System.out.println("Available conditions: temperature, weather, humidity, wind, clouds, rain");
        System.out.println("Time can be specified in minutes or hours");
        System.out.println("Examples:");
        System.out.println("- Determine the temperature in London in 10 minutes");
        System.out.println("- Determine the weather in Paris in 1 hour");
        System.out.println("- Determine the humidity in Tokyo in 30 minutes");
        System.out.println("Type 'exit' to quit");

        while (true) {
            System.out.print("\nEnter your query: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            processQuery(input);
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private void processQuery(String input) {
        input = input.replaceAll("[\\uFEFF\\u200B]", "").trim();
        
        Matcher matcher = QUERY_PATTERN.matcher(input);
        if (!matcher.matches()) {
            System.out.println("I can only process queries in the format: Determine the {condition} in {city} in {time}");
            return;
        }

        String condition = matcher.group(1).toLowerCase();
        String city = matcher.group(2).trim();
        String timeStr = matcher.group(3).trim();

        if (city.isEmpty()) {
            System.out.println("City name cannot be empty");
            return;
        }

        long minutes = extractMinutes(timeStr);
        if (minutes <= 0) {
            System.out.println("Invalid time format. Please specify a positive number of minutes or hours.");
            return;
        }

        if (minutes <= 60) {
            weatherService.getCurrentWeather(city)
                    .map(weather -> formatResponse(city, minutes, condition, weather.getMain()))
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("I couldn't determine the " + condition + " for " + city)
                    );
        } else if (minutes <= 1440) {
            weatherService.getForecast(city)
                    .map(forecast -> {
                        // Получаем прогноз на нужное время
                        int forecastIndex = (int) (minutes / 180) - 1; // 180 минут = 3 часа
                        if (forecastIndex < forecast.getList().size()) {
                            return formatResponse(city, minutes, condition, forecast.getList().get(forecastIndex));
                        }
                        return null;
                    })
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("I couldn't determine the " + condition + " for " + city)
                    );
        } else {
            System.out.println("I can only predict weather conditions for up to 24 hours in advance.");
        }
    }

    private long extractMinutes(String timeStr) {
        try {
            String[] parts = timeStr.split("\\s+");
            int value = Integer.parseInt(parts[0]);
            String unit = parts[1].toLowerCase();
            
            if (unit.startsWith("hour")) {
                return value * 60L;
            } else if (unit.startsWith("minute")) {
                return value;
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatResponse(String city, long minutes, String condition, WeatherComponents.Main main) {
        switch (condition) {
            case "temperature":
                return String.format("The temperature in %s will be %.1f°C in %d %s", 
                    city, main.getTemp(), 
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "weather":
                var currentWeather = weatherService.getCurrentWeather(city).get();
                StringBuilder weatherReport = new StringBuilder();
                weatherReport.append(String.format("Weather report for %s in %d %s:\n", 
                    city,
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                ));
                weatherReport.append(String.format("• Conditions: %s\n", currentWeather.getWeather().get(0).getDescription()));
                weatherReport.append(String.format("• Temperature: %.1f°C (feels like %.1f°C)\n", main.getTemp(), main.getFeelsLike()));
                weatherReport.append(String.format("• Humidity: %d%%\n", main.getHumidity()));
                weatherReport.append(String.format("• Wind Speed: %.1f m/s\n", currentWeather.getWind().getSpeed()));
                weatherReport.append(String.format("• Cloudiness: %d%%", currentWeather.getClouds().getAll()));
                
                var currentRain = currentWeather.getRain();
                if (currentRain != null) {
                    weatherReport.append(String.format("\n• Expected Rain: %.1f mm/h", currentRain.getOneHour()));
                }
                
                return weatherReport.toString();
            case "humidity":
                return String.format("The humidity in %s will be %d%% in %d %s", 
                    city, main.getHumidity(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "wind":
                return String.format("The wind speed in %s will be %.1f m/s in %d %s", 
                    city, weatherService.getCurrentWeather(city).get().getWind().getSpeed(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "clouds":
                return String.format("The cloudiness in %s will be %d%% in %d %s", 
                    city, weatherService.getCurrentWeather(city).get().getClouds().getAll(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "rain":
                WeatherComponents.Rain rain = weatherService.getCurrentWeather(city).get().getRain();
                if (rain != null) {
                    return String.format("The expected rain in %s will be %.1f mm in %d %s", 
                        city, rain.getOneHour(),
                        minutes >= 60 ? minutes / 60 : minutes,
                        minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                    );
                }
                return String.format("No rain expected in %s in %d %s", 
                    city,
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            default:
                return "Unsupported weather condition: " + condition;
        }
    }

    private String formatResponse(String city, long minutes, String condition, ForecastResponse.ForecastItem forecast) {
        WeatherComponents.Main main = forecast.getMain();
        WeatherComponents.Weather weather = forecast.getWeather().get(0);
        WeatherComponents.Wind wind = forecast.getWind();
        WeatherComponents.Clouds clouds = forecast.getClouds();
        WeatherComponents.Rain rain = forecast.getRain();

        switch (condition) {
            case "temperature":
                return String.format("The temperature in %s will be %.1f°C in %d %s", 
                    city, main.getTemp(), 
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "weather":
                StringBuilder weatherReport = new StringBuilder();
                weatherReport.append(String.format("Weather report for %s in %d %s:\n", 
                    city,
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                ));
                weatherReport.append(String.format("• Conditions: %s\n", weather.getDescription()));
                weatherReport.append(String.format("• Temperature: %.1f°C (feels like %.1f°C)\n", main.getTemp(), main.getFeelsLike()));
                weatherReport.append(String.format("• Humidity: %d%%\n", main.getHumidity()));
                weatherReport.append(String.format("• Wind Speed: %.1f m/s\n", wind.getSpeed()));
                weatherReport.append(String.format("• Cloudiness: %d%%", clouds.getAll()));
                
                if (rain != null) {
                    weatherReport.append(String.format("\n• Expected Rain: %.1f mm/3h", rain.getThreeHours()));
                }
                
                return weatherReport.toString();
            case "humidity":
                return String.format("The humidity in %s will be %d%% in %d %s", 
                    city, main.getHumidity(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "wind":
                return String.format("The wind speed in %s will be %.1f m/s in %d %s", 
                    city, wind.getSpeed(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "clouds":
                return String.format("The cloudiness in %s will be %d%% in %d %s", 
                    city, clouds.getAll(),
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            case "rain":
                if (rain != null) {
                    return String.format("The expected rain in %s will be %.1f mm in %d %s", 
                        city, rain.getThreeHours(),
                        minutes >= 60 ? minutes / 60 : minutes,
                        minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                    );
                }
                return String.format("No rain expected in %s in %d %s", 
                    city,
                    minutes >= 60 ? minutes / 60 : minutes,
                    minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
                );
            default:
                return "Unsupported weather condition: " + condition;
        }
    }

    public static void main(String[] args) {
        new WeatherAgent().start();
    }
} 