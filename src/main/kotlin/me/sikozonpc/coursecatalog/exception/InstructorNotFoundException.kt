package me.sikozonpc.coursecatalog.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class InstructorNotFoundException(private val id: Int) : NotFoundException(id.toString()) {
    override val message: String?
        get() = "Instructor with $id not found"
}