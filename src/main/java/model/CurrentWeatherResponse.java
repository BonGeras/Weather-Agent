package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeatherResponse {
    private WeatherComponents.Coord coord;
    private List<WeatherComponents.Weather> weather;
    private String base;
    private WeatherComponents.Main main;
    private int visibility;
    private WeatherComponents.Wind wind;
    private WeatherComponents.Clouds clouds;
    private WeatherComponents.Rain rain;
    private WeatherComponents.Sys sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;
    private long dt;

    public WeatherComponents.Coord getCoord() {
        return coord;
    }

    public void setCoord(WeatherComponents.Coord coord) {
        this.coord = coord;
    }

    public List<WeatherComponents.Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherComponents.Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public WeatherComponents.Main getMain() {
        return main;
    }

    public void setMain(WeatherComponents.Main main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public WeatherComponents.Wind getWind() {
        return wind;
    }

    public void setWind(WeatherComponents.Wind wind) {
        this.wind = wind;
    }

    public WeatherComponents.Clouds getClouds() {
        return clouds;
    }

    public void setClouds(WeatherComponents.Clouds clouds) {
        this.clouds = clouds;
    }

    public WeatherComponents.Rain getRain() {
        return rain;
    }

    public void setRain(WeatherComponents.Rain rain) {
        this.rain = rain;
    }

    public WeatherComponents.Sys getSys() {
        return sys;
    }

    public void setSys(WeatherComponents.Sys sys) {
        this.sys = sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }
} 