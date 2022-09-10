package me.sikozonpc.coursecatalog.repository

import me.sikozonpc.coursecatalog.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {
}