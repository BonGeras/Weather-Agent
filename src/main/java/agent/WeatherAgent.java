package agent;

import model.WeatherResponse;
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
                    .map(weather -> formatWeatherResponse(condition, weather, minutes))
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("I couldn't determine the " + condition + " for " + city)
                    );
        } else if (minutes <= 1440) {
            int forecastIndex = (int) (minutes / 180) - 1;
            if (forecastIndex < 0) forecastIndex = 0;

            weatherService.getForecast(city, forecastIndex)
                    .map(weather -> formatWeatherResponse(condition, weather, minutes))
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

    private String formatTimeString(long minutes) {
        return String.format("%d %s",
            minutes >= 60 ? minutes / 60 : minutes,
            minutes >= 60 ? (minutes / 60 == 1 ? "hour" : "hours") : (minutes == 1 ? "minute" : "minutes")
        );
    }

    private String formatWeatherResponse(String condition, WeatherResponse weather, long minutes) {
        String timeStr = formatTimeString(minutes);

        return switch (condition) {
            case "temperature" -> String.format("The temperature in %s will be %.1f°C in %s", 
                weather.getCityName(), weather.getTemperature(), timeStr);

            case "weather" -> {
                StringBuilder report = new StringBuilder();
                report.append(String.format("Weather report for %s in %s:\n", weather.getCityName(), timeStr));
                report.append(String.format("• Conditions: %s\n", weather.getDescription()));
                report.append(String.format("• Temperature: %.1f°C (feels like %.1f°C)\n", 
                    weather.getTemperature(), weather.getFeelsLike()));
                report.append(String.format("• Humidity: %d%%\n", weather.getHumidity()));
                report.append(String.format("• Wind Speed: %.1f m/s\n", weather.getWindSpeed()));
                report.append(String.format("• Cloudiness: %d%%", weather.getCloudiness()));

                Double rain = weather.getRain();
                if (rain != null) {
                    report.append(String.format("\n• Expected Rain: %.1f mm/%s", 
                        rain, weather.isCurrentWeather() ? "h" : "3h"));
                }
                yield report.toString();
            }

            case "humidity" -> String.format("The humidity in %s will be %d%% in %s", 
                weather.getCityName(), weather.getHumidity(), timeStr);

            case "wind" -> String.format("The wind speed in %s will be %.1f m/s in %s", 
                weather.getCityName(), weather.getWindSpeed(), timeStr);

            case "clouds" -> String.format("The cloudiness in %s will be %d%% in %s", 
                weather.getCityName(), weather.getCloudiness(), timeStr);

            case "rain" -> {
                Double rain = weather.getRain();
                yield rain != null
                    ? String.format("The expected rain in %s will be %.1f mm in %s", 
                        weather.getCityName(), rain, timeStr)
                    : String.format("No rain expected in %s in %s", weather.getCityName(), timeStr);
            }

            default -> "Unsupported weather condition: " + condition;
        };
    }

    public static void main(String[] args) {
        new WeatherAgent().start();
    }
} 