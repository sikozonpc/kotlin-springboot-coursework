package me.sikozonpc.coursecatalog.controller

import me.sikozonpc.coursecatalog.dto.InstructorDTO
import me.sikozonpc.coursecatalog.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import util.PostgresSQLContainerInitializer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InstructorControllerIntegrationTest : PostgresSQLContainerInitializer(){
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `should add a new instructor`() {
        val payload = InstructorDTO(null, "John Doe")

        val instructor = webTestClient.post()
            .uri(InstructorControllerPostMapping)
            .bodyValue(payload)
            .exchange()
            .expectStatus().isCreated
            .expectBody<InstructorDTO>()
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            // makes sure it was created in the db
            instructor!!.id != null
        }
    }

    @Test
    fun `should 400 if the payload is wrong`() {
        val payload = InstructorDTO(null, "")
        webTestClient.post()
            .uri(InstructorControllerPostMapping)
            .bodyValue(payload)
            .exchange()
            .expectStatus().is4xxClientError
    }
}