package me.sikozonpc.coursecatalog.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import util.PostgresSQLContainerInitializer
import util.courseEntityList
import util.instructorEntity
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryIntegrationTest : PostgresSQLContainerInitializer() {
    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    val instructor = instructorEntity()
    val courses = courseEntityList(instructor)

    @BeforeEach
    fun setUp() {
        instructorRepository.save(instructor)
        courseRepository.deleteAll()
        courseRepository.saveAll(courses)
    }

    @ParameterizedTest
    @MethodSource("setupCourseAndSizeData")
    fun `findByNameContaining() parameterized`(name: String, expectedSize: Int) {
        val foundCourses = courseRepository.findByNameContaining(name)
        Assertions.assertEquals(expectedSize, foundCourses.size)
    }

    @ParameterizedTest
    @MethodSource("setupCourseAndSizeData")
    fun `findCoursesByName() parameterized`(name: String, expectedSize: Int) {
        val foundCourses = courseRepository.findCoursesByName(name)
        Assertions.assertEquals(expectedSize, foundCourses.size)
    }

    companion object {
        @JvmStatic
        fun setupCourseAndSizeData(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("Spring", 2),
                Arguments.arguments("Wiremock", 1),
            )
        }
    }
}











