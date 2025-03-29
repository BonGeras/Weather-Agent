package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    private String cod;
    private double message;
    private int cnt;
    private List<ForecastItem> list;
    private City city;

    public static class ForecastItem {
        private long dt;
        private WeatherComponents.Main main;
        private List<WeatherComponents.Weather> weather;
        private WeatherComponents.Clouds clouds;
        private WeatherComponents.Wind wind;
        private int visibility;
        private double pop;
        private WeatherComponents.Sys sys;
        @JsonProperty("dt_txt")
        private String dtTxt;
        private WeatherComponents.Rain rain;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public WeatherComponents.Main getMain() {
            return main;
        }

        public void setMain(WeatherComponents.Main main) {
            this.main = main;
        }

        public List<WeatherComponents.Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherComponents.Weather> weather) {
            this.weather = weather;
        }

        public WeatherComponents.Clouds getClouds() {
            return clouds;
        }

        public void setClouds(WeatherComponents.Clouds clouds) {
            this.clouds = clouds;
        }

        public WeatherComponents.Wind getWind() {
            return wind;
        }

        public void setWind(WeatherComponents.Wind wind) {
            this.wind = wind;
        }

        public int getVisibility() {
            return visibility;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public double getPop() {
            return pop;
        }

        public void setPop(double pop) {
            this.pop = pop;
        }

        public WeatherComponents.Sys getSys() {
            return sys;
        }

        public void setSys(WeatherComponents.Sys sys) {
            this.sys = sys;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }

        public WeatherComponents.Rain getRain() {
            return rain;
        }

        public void setRain(WeatherComponents.Rain rain) {
            this.rain = rain;
        }
    }

    public static class City {
        private int id;
        private String name;
        private WeatherComponents.Coord coord;
        private String country;
        private int population;
        private int timezone;
        private long sunrise;
        private long sunset;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public WeatherComponents.Coord getCoord() {
            return coord;
        }

        public void setCoord(WeatherComponents.Coord coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getPopulation() {
            return population;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ForecastItem> getList() {
        return list;
    }

    public void setList(List<ForecastItem> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
} 