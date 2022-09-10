package me.sikozonpc.coursecatalog.dto

import javax.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,

    @get:NotBlank(message = "Name must not be empty")
    val name: String,
)