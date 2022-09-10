package me.sikozonpc.coursecatalog.service

import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.entity.Course
import me.sikozonpc.coursecatalog.exception.CourseNotFoundException
import me.sikozonpc.coursecatalog.exception.InstructorNotFoundException
import me.sikozonpc.coursecatalog.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService,
) {
    companion object : KLogging()

    fun add(dto: CourseDTO): CourseDTO {
        val instructor = instructorService.findByID(dto.instructorId!!)
        if (!instructor.isPresent) throw InstructorNotFoundException(dto.instructorId)

        val entity = Course(null, dto.name, dto.category, instructor.get())

        logger.info("Saving course: $entity")

        return courseRepository.save(entity).let {
            CourseDTO(it.id, it.name, it.category, it.instructor?.id)
        }
    }

    fun getAll(courseName: String?): List<CourseDTO> {
        val courses = courseName?.let {
            courseRepository.findCoursesByName(courseName)
        } ?: courseRepository.findAll()

        return courses.map { CourseDTO(it.id, it.name, it.category) }

    }

    fun update(id: Int, updated: CourseDTO): CourseDTO {
        val course = courseRepository.findById(id)
        return if (course.isPresent) {
            course.get().let {
                it.name = updated.name
                it.category = updated.category

                courseRepository.save(it)

                CourseDTO(it.id, it.name, it.category)
            }
        } else throw CourseNotFoundException(id)
    }

    fun delete(id: Int) {
        val course = courseRepository.findById(id)
        if (course.isPresent) {
            course.get().let { courseRepository.deleteById(id) }
        } else throw CourseNotFoundException(id)
    }

}
