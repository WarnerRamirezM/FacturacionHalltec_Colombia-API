package com.hernan.oauth_client.Services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthService {
    @Value("${api.url}")  // Asumiendo que la URL está configurada en el archivo application.properties
    private String apiUrl;

    @Value("${api.client-id}")
    private String clientId;

    @Value("${api.client-secret}")
    private String clientSecret;

    @Value("${api.username}")  // Correo electrónico del usuario
    private String email;

    @Value("${api.password}")  // Contraseña del usuario
    private String password;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {
        // Cuerpo de la solicitud con el formato adecuado (application/x-www-form-urlencoded)
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("grant_type", "password")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("username", email)  // Usamos el email del entorno
                .queryParam("password", password);  // Usamos la contraseña del entorno

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // Especificamos que el cuerpo está en formato URL-encoded
        headers.set("Accept", "application/json");  // Especificamos que esperamos una respuesta JSON
        HttpEntity<String> entity = new HttpEntity<>(builder.build().getQuery(), headers);

        // Realizamos la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,  // URL completa para obtener el token
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            // Parseamos la respuesta JSON para obtener el token de acceso
            String accessToken = parseAccessToken(response.getBody());
            return accessToken;
        } else {
            throw new RuntimeException("Error al obtener el token: " + response.getStatusCode());
        }
    }

    // Método para extraer el access_token de la respuesta JSON
    private String parseAccessToken(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear la respuesta JSON", e);
        }
    }



}
