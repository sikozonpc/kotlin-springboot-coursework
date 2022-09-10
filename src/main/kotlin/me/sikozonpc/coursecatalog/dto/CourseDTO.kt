package me.sikozonpc.coursecatalog.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CourseDTO(
    val id: Int?,

    @get:NotBlank(message = "name must not be empty")
    val name: String,

    @get:NotBlank(message = "category must not be empty")
    val category: String,

    @get:NotNull(message = "instructorId must not be empty")
    val instructorId: Int? = null,
)