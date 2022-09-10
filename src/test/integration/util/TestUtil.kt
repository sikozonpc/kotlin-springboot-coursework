package util

import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.dto.InstructorDTO
import me.sikozonpc.coursecatalog.entity.Course
import me.sikozonpc.coursecatalog.entity.Instructor

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Dilip Sundarraj",
    instructorId: Int? = 1,
) = CourseDTO(
    id,
    name,
    category,
    instructorId
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development",
        instructor),
    Course(null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development", instructor
    ),
    Course(null,
        "Wiremock for Java Developers", "Development",
        instructor)
)

fun instructorDTO(id: Int? = null, name: String = "John Doe") = InstructorDTO(id, name)

fun instructorEntity(name: String = "Dilip Sundarraj") = Instructor(null, name)

