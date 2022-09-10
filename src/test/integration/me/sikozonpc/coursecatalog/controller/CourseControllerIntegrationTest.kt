package me.sikozonpc.coursecatalog.controller

import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.repository.CourseRepository
import me.sikozonpc.coursecatalog.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.junit.jupiter.Testcontainers
import util.PostgresSQLContainerInitializer
import util.courseEntityList
import util.instructorEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseControllerIntegrationTest: PostgresSQLContainerInitializer() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    val instructor = instructorEntity()

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun `should add a new course`() {
        val payload = CourseDTO(null, "some name", "some category", instructor.id)

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
            course!!.id != null
        }
    }

    @Test
    fun `should get all courses`() {
        val courses = webTestClient.get()
            .uri(CourseControllerPostMapping)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList<CourseDTO>()
            .returnResult()
            .responseBody

        val addedCourses = courseEntityList()
        assertEquals(addedCourses.size, courses!!.size)

        courses.forEach {
            Assertions.assertTrue { it.id != null }
        }
    }

    @Test
    fun `should get all courses by name`() {
        val uri = UriComponentsBuilder.fromUriString(CourseControllerPostMapping)
            .queryParam("course_name", "Spring")
            .toUriString()

        val courses = webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList<CourseDTO>()
            .returnResult()
            .responseBody

        assertEquals(2, courses!!.size)

        courses.forEach {
            Assertions.assertTrue { it.id != null }
        }
    }

    @Test
    fun `should update the course`() {
        val firstCourse = courseRepository.findAll().firstOrNull()
        val payload = CourseDTO(null, "updated name", "updated category")

        val course = webTestClient.put()
            .uri("$CourseControllerPostMapping/${firstCourse!!.id}")
            .bodyValue(payload)
            .exchange()
            .expectStatus().isOk
            .expectBody<CourseDTO>()
            .returnResult()
            .responseBody!!

        // fetching again to make sure it actually updated
        val updatedCourse = courseRepository.findByIdOrNull(firstCourse.id!!)
        assertEquals(updatedCourse!!.name, payload.name)
        assertEquals(course.name, payload.name)
    }

    @Test
    fun `should throw a 404 when no course is found whilst updating`() {
        val payload = CourseDTO(null, "updated name", "updated category")

        webTestClient.put()
            .uri("$CourseControllerPostMapping/42069")
            .bodyValue(payload)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should delete a course`() {
        val firstCourse = courseRepository.findAll().firstOrNull()

        webTestClient.delete()
            .uri("$CourseControllerPostMapping/${firstCourse!!.id}")
            .exchange()
            .expectStatus().isNoContent

        val deletedCourse = courseRepository.findById(firstCourse.id!!)
        if (deletedCourse.isPresent) fail<String>("Deleted course still exists")
    }
}