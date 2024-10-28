package com.example.weatherapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class WeatherController {

    @Value("${OPENWEATHER_API_KEY}")
    private String apiKey; // Inject API key from environment variable

    private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";

    @GetMapping("/weather")
    public Map<String, Object> getWeather(@RequestParam String city) {
        RestTemplate restTemplate = new RestTemplate();

        @SuppressWarnings("unchecked")
		Map<String, Object> response = restTemplate.getForObject(
                WEATHER_API_URL, Map.class, city, apiKey);

        @SuppressWarnings("unchecked")
		Map<String, Object> main = (Map<String, Object>) response.get("main");
        @SuppressWarnings("unchecked")
		Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        @SuppressWarnings("unchecked")
		Map<String, Object> weatherDetails = ((List<Map<String, Object>>) response.get("weather")).get(0);

        return Map.of(
                "city", city,
                "temperature", main.get("temp") + "°C",
                "humidity", main.get("humidity") + "%",
                "description", weatherDetails.get("description"),
                "windSpeed", wind.get("speed") + " km/h"
        );
    }
}
