package me.sikozonpc.coursecatalog.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class CourseNotFoundException(private val id: Int) : NotFoundException(id.toString()) {
    override val message: String?
        get() = "Course with $id not found"
}