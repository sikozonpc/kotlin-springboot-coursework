package me.sikozonpc.coursecatalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.dto.InstructorDTO
import me.sikozonpc.coursecatalog.service.InstructorService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import util.instructorDTO


@WebMvcTest(controllers = [InstructorController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMock: InstructorService

    @Test
    fun `INSERT Instructor`() {
        every { instructorServiceMock.add(any()) } returns instructorDTO(id = 1)

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
            instructor!!.id == 1
        }
    }
}