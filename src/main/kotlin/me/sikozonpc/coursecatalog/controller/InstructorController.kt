package me.sikozonpc.coursecatalog.controller

import me.sikozonpc.coursecatalog.dto.InstructorDTO
import me.sikozonpc.coursecatalog.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

const val InstructorControllerPostMapping = "/v1/instructors";

@RestController
@RequestMapping(InstructorControllerPostMapping)
@Validated
class InstructorController(
    val instructorService: InstructorService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addInstructor(
        @RequestBody @Valid dto: InstructorDTO,
    ): InstructorDTO = instructorService.add(dto)
}