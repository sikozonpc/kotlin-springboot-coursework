package me.sikozonpc.coursecatalog.service

import me.sikozonpc.coursecatalog.dto.InstructorDTO
import me.sikozonpc.coursecatalog.entity.Instructor
import me.sikozonpc.coursecatalog.repository.InstructorRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(
    val instructorRepository: InstructorRepository
) {
    companion object : KLogging()

    fun add(dto: InstructorDTO): InstructorDTO {
        val entity = Instructor(null, dto.name)
        logger.info("Saving Instructor: $entity")
        return instructorRepository.save(entity).let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findByID(id: Int): Optional<Instructor> = instructorRepository.findById(id)
}