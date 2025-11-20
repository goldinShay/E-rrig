package org.errig.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    public double fetchExternalAirTemp() {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=52.160&longitude=4.490&current_weather=true";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> current = (Map<String, Object>) response.get("current_weather");
            return (Double) current.get("temperature");
        } catch (Exception e) {
            return 0.0;
        }
    }

    // Convenience wrapper
    public double getCurrentTemperature(String location) {
        // For now, ignore location and always fetch Leiden
        return fetchExternalAirTemp();
    }
}

