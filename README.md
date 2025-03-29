# Weather Agent CLI

A command-line interface application that simulates an AI weather agent using the OpenWeatherMap API.

## Features

- Get current weather information for any location
- Predict temperature changes for specified time periods
- Simple and intuitive command-line interface
- Uses real weather data from OpenWeatherMap API

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
Determine the temperature in {location} in {time}
```

Examples:
- `Determine the temperature in London in 1 hour`
- `Determine the temperature in New York in 2 hours`
- `Determine the temperature in Tokyo in 3 hours`

### Query Format Rules

1. The query must start with "Determine the temperature in"
2. Location can be any city name
3. Time must be specified in hours (e.g., "1 hour" or "2 hours")
4. Time must be between 1 and 48 hours

### Example Session

```
Weather Agent started. Enter your query in the format:
Determine the temperature in {location} in {time}
Example: Determine the temperature in London in 1 hour
Type 'exit' to quit

Enter your query: Determine the temperature in London in 1 hour
Response: The temperature in London in 1 hour will be 15.2°C

Enter your query: Determine the temperature in New York in 2 hours
Response: The temperature in New York in 2 hours will be 18.5°C

Enter your query: exit
```

## Error Handling

The application will display appropriate error messages if:
- The query format is incorrect
- The location is not found
- The time format is invalid
- The time is outside the allowed range (1-48 hours)

## Note

The temperature prediction is simulated based on the current temperature and random variations. In a real application, this would use actual forecast data from the weather API. 