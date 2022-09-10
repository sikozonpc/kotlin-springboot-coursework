package me.sikozonpc.coursecatalog.controller

import me.sikozonpc.coursecatalog.dto.CourseDTO
import me.sikozonpc.coursecatalog.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

const val CourseControllerPostMapping = "/v1/courses"

@RestController
@RequestMapping(CourseControllerPostMapping)
@Validated
class CourseController(
    val courseService: CourseService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(
        @RequestBody @Valid courseDTO: CourseDTO,
    ): CourseDTO = courseService.add(courseDTO)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam("course_name", required = false) courseName: String?,
    ): List<CourseDTO> = courseService.getAll(courseName)

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @RequestBody updated: CourseDTO,
        @PathVariable("courseId") courseId: Int,
    ): CourseDTO = courseService.update(courseId, updated)

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable("courseId") courseId: Int,
    ) = courseService.delete(courseId)

}