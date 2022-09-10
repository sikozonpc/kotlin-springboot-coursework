package me.sikozonpc.coursecatalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.service.CourseService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import util.courseDTO


@WebMvcTest(controllers = [CourseController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun `INSERT Course`() {
        every { courseServiceMock.add(any()) } returns courseDTO(id = 1)

        val payload = CourseDTO(null, "some name", "some category", 1)

        val course = webTestClient.post()
            .uri(CourseControllerPostMapping)
            .bodyValue(payload)
            .exchange()
            .expectStatus().isCreated
            .expectBody<CourseDTO>()
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            // makes sure it was created in the db
            course!!.id == 1
        }
    }

    @Test
    fun `INSERT Course VALIDATIONS`() {
        every { courseServiceMock.add(any()) } returns courseDTO(id = 1)

        val payload = CourseDTO(null, "", "", 1)

        val response = webTestClient.post()
            .uri(CourseControllerPostMapping)
            .bodyValue(payload)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody<String>()
            .returnResult()
            .responseBody

        Assertions.assertEquals("category must not be empty, name must not be empty", response)
    }

    @Test
    fun `INSERT Course RUNTIME EXPECTION`() {
        val payload = CourseDTO(null, "some name", "some category", 1)

        val errorMsg = "unexpected error occurred"
        every { courseServiceMock.add(any()) } throws RuntimeException(errorMsg)

        val response = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(payload)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody<String>()
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMsg, response)
    }

    @Test
    fun `GET ALL Courses`() {
        every { courseServiceMock.getAll(any()) } returns listOf(courseDTO(id = 1), courseDTO(id = 2))

        val courses = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<CourseDTO>()
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courses!!.size)

        courses.forEach {
            Assertions.assertTrue { it.id != null }
        }
    }

    @Test
    fun `UPDATE course`() {
        val payload = CourseDTO(null, "updated name", "updated category", 1)
        every { courseServiceMock.update(any(), payload) } returns courseDTO(1, payload.name, payload.category)

        val course = webTestClient.put()
            .uri("/v1/courses/1")
            .bodyValue(payload)
            .exchange()
            .expectStatus().isOk
            .expectBody<CourseDTO>()
            .returnResult()
            .responseBody!!

        Assertions.assertEquals(course.name, payload.name)
    }

    @Test
    fun `DELETE Course`() {
        every { courseServiceMock.delete(any()) } just runs

        webTestClient.delete()
            .uri("/v1/courses/1")
            .exchange()
            .expectStatus().isNoContent
    }
}