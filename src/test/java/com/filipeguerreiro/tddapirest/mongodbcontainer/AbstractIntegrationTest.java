package com.filipeguerreiro.tddapirest.mongodbcontainer;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import java.io.IOException;

@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withCommand("--replSet", "rs0");

    @BeforeAll
    static void initReplicaSet() throws IOException, InterruptedException {
        mongoDBContainer.execInContainer("/bin/bash", "-c",
                "mongosh --eval 'rs.initiate(); while (rs.status().ok && rs.status().myState !== 1) { print(\"Waiting for primary...\"); sleep(1000); }'");
    }

}
