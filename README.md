# Weather Agent CLI

A command-line interface application that provides weather information and forecasts using the OpenWeatherMap API.

## Features

- Get current weather information for any location
- Get weather forecasts for up to 24 hours in advance
- Query various weather conditions (temperature, humidity, wind, clouds, rain)
- Simple and intuitive command-line interface
- Uses real-time data from OpenWeatherMap API

## Prerequisites

- Java 17 or later
- Maven
- OpenWeatherMap API key (already configured in the application)

## Building the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the following command to build the application:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory named `weather-agent-1.0-SNAPSHOT-jar-with-dependencies.jar`.

## Running the Application

Run the application using the following command:

```bash
java -jar target/weather-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Usage

The application accepts queries in the following format:
```
Determine the {condition} in {location} in {time}
```

Where:
- `{condition}` is one of: temperature, weather, humidity, wind, clouds, rain
- `{location}` is any city name
- `{time}` is specified in minutes or hours (e.g., "30 minutes", "1 hour", "3 hours")

Examples:
- `Determine the temperature in London in 1 hour`
- `Determine the weather in Paris in 30 minutes`
- `Determine the humidity in Tokyo in 2 hours`
- `Determine the wind in New York in 45 minutes`
- `Determine the clouds in Berlin in 3 hours`
- `Determine the rain in Sydney in 6 hours`

### Time Range Limitations

- For forecasts up to 60 minutes, the application uses current weather data
- For forecasts from 1 hour to 24 hours, the application uses forecast data from OpenWeatherMap API
- The maximum forecast time is 24 hours

### Example Session

```
Weather Agent started. I can help you determine various weather conditions in any city.
Please use the following format:
Determine the {condition} in {city} in {time}
Available conditions: temperature, weather, humidity, wind, clouds, rain
Time can be specified in minutes or hours
Examples:
- Determine the temperature in London in 10 minutes
- Determine the weather in Paris in 1 hour
- Determine the humidity in Tokyo in 30 minutes
Type 'exit' to quit

Enter your query: Determine the temperature in London in 1 hour
The temperature in London will be 15.2°C in 1 hour

Enter your query: Determine the weather in Paris in 2 hours
Weather report for Paris in 2 hours:
• Conditions: scattered clouds
• Temperature: 18.5°C (feels like 17.8°C)
• Humidity: 65%
• Wind Speed: 3.5 m/s
• Cloudiness: 40%

Enter your query: exit
Goodbye!
```

## Error Handling

The application will display appropriate error messages if:
- The query format is incorrect
- The location is not found
- The time format is invalid
- The time is outside the allowed range (up to 24 hours)

## Technical Implementation

The application uses:
- OpenWeatherMap's current weather API for short-term forecasts (up to 60 minutes)
- OpenWeatherMap's forecast API for medium-term forecasts (1-24 hours)
- All weather data is fetched in real-time from the API, not simulated 