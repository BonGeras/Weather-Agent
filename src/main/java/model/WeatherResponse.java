package model;

public class WeatherResponse {
    private final double temperature;
    private final double feelsLike;
    private final int humidity;
    private final double windSpeed;
    private final int cloudiness;
    private final Double rain;
    private final String description;
    private final String cityName;
    private final boolean isCurrentWeather;

    private WeatherResponse(Builder builder) {
        this.temperature = builder.temperature;
        this.feelsLike = builder.feelsLike;
        this.humidity = builder.humidity;
        this.windSpeed = builder.windSpeed;
        this.cloudiness = builder.cloudiness;
        this.rain = builder.rain;
        this.description = builder.description;
        this.cityName = builder.cityName;
        this.isCurrentWeather = builder.isCurrentWeather;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public Double getRain() {
        return rain;
    }

    public String getDescription() {
        return description;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isCurrentWeather() {
        return isCurrentWeather;
    }

    public static class Builder {
        private double temperature;
        private double feelsLike;
        private int humidity;
        private double windSpeed;
        private int cloudiness;
        private Double rain;
        private String description;
        private String cityName;
        private boolean isCurrentWeather;

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder feelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public Builder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder windSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder cloudiness(int cloudiness) {
            this.cloudiness = cloudiness;
            return this;
        }

        public Builder rain(Double rain) {
            this.rain = rain;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder cityName(String cityName) {
            this.cityName = cityName;
            return this;
        }

        public Builder isCurrentWeather(boolean isCurrentWeather) {
            this.isCurrentWeather = isCurrentWeather;
            return this;
        }

        public WeatherResponse build() {
            return new WeatherResponse(this);
        }
    }
} 