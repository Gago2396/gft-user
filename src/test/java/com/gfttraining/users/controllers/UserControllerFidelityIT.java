package com.gfttraining.users.controllers;

import com.gfttraining.users.repositories.CartRepository;
import com.gfttraining.users.services.FavoriteService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserControllerFidelityIT {
    @LocalServerPort
    private int port;
    private WebTestClient client;
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CartRepository cartRepository;

    public static WireMockServer wireMockServer = new WireMockServer(8085);


    @BeforeAll
    static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    public UserControllerFidelityIT() {
    }

    //Initiate the web client
    @PostConstruct
    void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
    }

    @Test
    @DisplayName("Update User Fidelity Points by ID")
    @Order(1)
    void updateFidelityPointsTest() {

        String cartJsonTemplate = "{ \"id\": %d, \"userId\": %d, \"status\": \"%s\", \"finalPrice\": %d, \"finalWeight\": %d }";

        Long user = 1L;

        List<String> cartJsonList = new ArrayList<>();
        cartJsonList.add(String.format(cartJsonTemplate, 1, 1, "DRAFT", 0, 0));
        cartJsonList.add(String.format(cartJsonTemplate, 2, 1, "SUBMITTED", 10, 5));
        cartJsonList.add(String.format(cartJsonTemplate, 3, 1, "SUBMITTED", 75, 5));
        cartJsonList.add(String.format(cartJsonTemplate, 4, 1, "SUBMITTED", 120, 5));


        String responseBody = "[" + String.join(",", cartJsonList) + "]";

        System.out.println(responseBody);

        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/carts/" + user))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        client.get().uri("/users/1/fidelity-points/")
                .exchange()
                .expectStatus().isOk();
    }
}
