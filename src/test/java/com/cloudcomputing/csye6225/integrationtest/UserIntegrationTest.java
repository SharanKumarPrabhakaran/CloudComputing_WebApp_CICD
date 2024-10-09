package com.cloudcomputing.csye6225.integrationtest;

import com.cloudcomputing.csye6225.model.User;
import com.cloudcomputing.csye6225.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Test
    public void testCreateUserDetailsAndValidateUserDetails() {

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Create an User record
        given()
                .contentType("application/json")
                .body("{ \"first_name\": \"Jane\", \"last_name\": \"Doe\", \"password\": \"test\", \"username\": \"psk141996@gmail.com\" }")
                .when()
                .post("/v5/user")
                .then()
                .statusCode(201);

        User userFromDb = userRepository.findByUsername("psk141996@gmail.com");
        userFromDb.setUserVerified(true);
        userRepository.save(userFromDb);

        // Validate User details existence
        given()
                .auth().preemptive().basic("psk141996@gmail.com", "test")
                .when()
                .get("/v5/user/self")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Jane"))
                .body("lastName", equalTo("Doe"))
                .body("username", equalTo("psk141996@gmail.com"));
    }

    @Test
    public void testUpdateUserDetailsAndValidateUpdateUserDetails() {
        // Update the User record
        given()
                .auth().preemptive().basic("psk141996@gmail.com", "test")
                .contentType("application/json")
                .body("{ \"first_name\": \"Jane_changed\", \"last_name\": \"Doe_changed\", \"password\": \"test_changed\" }")
                .when()
                .put("/v5/user/self")
                .then()
                .statusCode(204);

        // Validate updated User details
        given()
                .auth().preemptive().basic("psk141996@gmail.com", "test_changed")
                .when()
                .get("/v5/user/self")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Jane_changed"))
                .body("lastName", equalTo("Doe_changed"))
                .body("username", equalTo("psk141996@gmail.com"));
    }
}

